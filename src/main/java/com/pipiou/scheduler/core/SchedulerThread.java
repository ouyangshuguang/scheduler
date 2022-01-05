package com.pipiou.scheduler.core;

import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.exception.SchedulerException;
import com.pipiou.scheduler.exception.ThreadPoolRejectedException;
import com.pipiou.scheduler.simpl.JobTriggerBundle;
import com.pipiou.scheduler.spi.OperableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class SchedulerThread extends Thread {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private SchedulerResources resources;

    private Scheduler scheduler;

    private final Object sigLock = new Object();

    private AtomicBoolean halted;

    private boolean paused;

    private boolean signaled;

    private static long DEFAULT_IDLE_WAIT_TIME = 30L * 1000L;

    private long idleWaitTime = DEFAULT_IDLE_WAIT_TIME;

    public void setIdleWaitTime(long idleWaitTime) {
        this.idleWaitTime = idleWaitTime;
    }

    private Random random = new Random();

    private int idleWaitVariable = 10000;

    public SchedulerThread(Scheduler scheduler, SchedulerResources resources) {
        this.scheduler = scheduler;
        this.resources = resources;
        this.halted = new AtomicBoolean(false);
        paused = true;
    }

    @Override
    public void run() {
        while (!halted.get()) {
            try {
                synchronized (sigLock) {
                    while (paused && !halted.get()) {
                        try {
                            sigLock.wait(1000L);
                        } catch (InterruptedException ignore) {
                        }
                    }

                    if (halted.get()) {
                        break;
                    }
                }
                clearSignalScheduleChange();
                long now = System.currentTimeMillis();
                List<OperableTrigger> triggers = resources.getJobStore().acquireNextTriggers(now + idleWaitTime);
                if (triggers != null && !triggers.isEmpty()) {
                    now = System.currentTimeMillis();
                    long triggerTime = triggers.get(0).getNextFireTime().getTime();
                    long timeUntilTrigger = triggerTime - now;
                    while (timeUntilTrigger > 2) {
                        synchronized (sigLock) {
                            if (halted.get()) {
                                break;
                            }
                            try {
                                now = System.currentTimeMillis();
                                timeUntilTrigger = triggerTime - now;
                                if (timeUntilTrigger >= 1) {
                                    sigLock.wait(timeUntilTrigger);
                                }
                            } catch (InterruptedException ignore) {
                            }
                        }
                        if (releaseTriggersIfSginalScheduleChange(triggers)) {
                            break;
                        }
                        now = System.currentTimeMillis();
                        timeUntilTrigger = triggerTime - now;
                    }
                    if(triggers.isEmpty()) {
                        continue;
                    }
                    synchronized (sigLock) {
                        if (halted.get()) {
                            break;
                        }
                    }
                    List<JobTriggerBundle> jobTriggerBundles = resources.getJobStore().fireTriggers(triggers);
                    for (JobTriggerBundle jobTriggerBundle : jobTriggerBundles) {
                        RunShell jobRunShell;
                        try {
                            jobRunShell = resources.getRunShellFactory().createJobRunShell(scheduler, jobTriggerBundle);
                        } catch (SchedulerException e) {
                            resources.getJobStore().completeTrigger(jobTriggerBundle.getTriggerWrapper().trigger, Trigger.ExecutionState.SET_SHELL_CREATE_ERROR);
                            continue;
                        }
                        try {
                            if (resources.getThreadPool().runInThread(jobRunShell) == false) {
                                resources.getJobStore().completeTrigger(jobTriggerBundle.getTriggerWrapper().trigger, Trigger.ExecutionState.SET_SHELL_RUN_ERROR);
                            }
                        } catch (ThreadPoolRejectedException e) {

                        }
                    }
                    continue;
                }

                now = System.currentTimeMillis();
                long waitTime = now + getRandomizedIdleWaitTime();
                long timeUntilContinue = waitTime - now;
                synchronized (sigLock) {
                    try {
                        if (!halted.get()) {
                            if (!isScheduleChanged()) {
                                sigLock.wait(timeUntilContinue);
                            }
                        }
                    } catch (InterruptedException ignore) {
                    }
                }
            } catch (RuntimeException re) {
                log.error("Runtime error occurred in SchedulerThread.", re);
            }
        }
    }

    public void halt() {
        synchronized (sigLock) {
            halted.set(true);
            if (paused) {
                sigLock.notifyAll();
            } else {
                signalScheduleChange();
            }
        }
    }

    public void togglePause(boolean pause) {
        synchronized (sigLock) {
            paused = pause;
            if (paused) {
                signalScheduleChange();
            } else {
                sigLock.notifyAll();
            }
        }
    }

    public boolean releaseTriggersIfSginalScheduleChange(List<OperableTrigger> triggers) {
        if (isScheduleChanged()) {
            for (OperableTrigger trigger : triggers) {
                resources.getJobStore().releaseAcquiredTrigger(trigger);
            }
            return true;
        }
        return false;
    }


    public boolean isScheduleChanged() {
        synchronized (sigLock) {
            return signaled;
        }
    }

    public void signalScheduleChange() {
        synchronized (sigLock) {
            signaled = true;
            sigLock.notifyAll();
        }
    }

    public void clearSignalScheduleChange() {
        synchronized (sigLock) {
            signaled = false;
        }
    }

    private long getRandomizedIdleWaitTime() {
        return idleWaitTime - random.nextInt(idleWaitVariable);
    }
}

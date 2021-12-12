package com.pipiou.scheduler.simpl;

import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.JobKey;
import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.TriggerKey;
import com.pipiou.scheduler.core.SchedulerThread;
import com.pipiou.scheduler.exception.ObjectAlreadyExistsException;
import com.pipiou.scheduler.spi.JobStore;
import com.pipiou.scheduler.spi.OperableTrigger;
import com.pipiou.scheduler.utils.TriggerWrapperComparator;

import java.util.*;

public class RAMJobStore implements JobStore {

    protected SchedulerThread schedulerThread;

    private final Object lock = new Object();

    private HashMap<JobKey, JobWrapper> jobWrappersByKey = new HashMap<JobKey, JobWrapper>(1000);

    private HashMap<TriggerKey, TriggerWrapper> triggerWrappersByKey = new HashMap<TriggerKey, TriggerWrapper>(1000);

    private HashMap<JobKey, List<TriggerWrapper>> triggerWrappersByJob = new HashMap<JobKey, List<TriggerWrapper>>(1000);

    private TreeSet<TriggerWrapper> acquiredTriggers = new TreeSet<TriggerWrapper>(new TriggerWrapperComparator());

    protected long misfireThreshold = 5000L;

    @Override
    public void storeJob(JobDetail jobDetail, boolean replaceExisting) throws ObjectAlreadyExistsException {
        synchronized (lock) {
            if (jobWrappersByKey.get(jobDetail.getKey()) != null) {
                if (!replaceExisting) {
                    throw new ObjectAlreadyExistsException(jobDetail);
                }
            }
            jobWrappersByKey.put(jobDetail.getKey(), new JobWrapper(jobDetail));
        }
    }

    @Override
    public boolean removeJob(JobKey key) {
        boolean found;

        synchronized (lock) {
            List<TriggerWrapper> triggers = triggerWrappersByJob.get(key);
            for (TriggerWrapper tw : triggers) {
                this.removeTrigger(tw.trigger.getKey());
            }
            found = jobWrappersByKey.remove(key) != null;
        }
        return found;
    }

    @Override
    public void storeTrigger(OperableTrigger trigger, boolean replaceExisting) throws ObjectAlreadyExistsException {
        synchronized (lock) {
            if (triggerWrappersByKey.get(trigger.getKey()) != null) {
                if (!replaceExisting) {
                    throw new ObjectAlreadyExistsException(trigger);
                }
            }
            TriggerWrapper tw = new TriggerWrapper(trigger);
            triggerWrappersByKey.put(trigger.getKey(), tw);
            List<TriggerWrapper> triggers = triggerWrappersByJob.get(trigger.getJobKey());
            if (triggers == null) {
                triggers = new ArrayList<TriggerWrapper>();
                triggerWrappersByJob.put(trigger.getJobKey(), triggers);
            }
            triggers.add(tw);
            acquiredTriggers.add(tw);
        }
    }

    @Override
    public boolean removeTrigger(TriggerKey key) {
        boolean found;

        synchronized (lock) {
            TriggerWrapper trigger = triggerWrappersByKey.remove(key);
            found = trigger != null;
            if (found) {
                acquiredTriggers.remove(trigger);
            }
        }
        return found;
    }

    @Override
    public void storeJobAndTrigger(JobDetail newJob, OperableTrigger newTrigger) throws ObjectAlreadyExistsException {
        storeJob(newJob, false);
        storeTrigger(newTrigger, false);
    }

    @Override
    public List<OperableTrigger> acquireNextTriggers(long noLaterThan) {
        synchronized (lock) {
            List<OperableTrigger> result = new ArrayList<OperableTrigger>();
            if (acquiredTriggers.size() == 0) {
                return result;
            }

            while (true) {
                TriggerWrapper tw;
                try {
                    tw = acquiredTriggers.first();
                    if (tw == null) {
                        break;
                    }
                    acquiredTriggers.remove(tw);
                } catch (java.util.NoSuchElementException nsee) {
                    break;
                }
                if (tw.trigger.getNextFireTime() == null) {
                    continue;
                }
                if (applyMisfire(tw.trigger)) {
                    acquiredTriggers.add(tw);
                    continue;
                }
                if (tw.trigger.getNextFireTime().getTime() > noLaterThan) {
                    acquiredTriggers.add(tw);
                    break;
                }
                tw.state = TriggerWrapper.STATE_ACQUIRED;
                result.add(tw.trigger);
            }
            return result;
        }
    }

    @Override
    public void releaseAcquiredTrigger(OperableTrigger trigger) {
        synchronized (lock) {
            TriggerWrapper tw = triggerWrappersByKey.get(trigger.getKey());
            if (tw != null && tw.state == TriggerWrapper.STATE_ACQUIRED) {
                tw.state = TriggerWrapper.STATE_WAITING;
                acquiredTriggers.add(tw);
            }
        }
    }

    @Override
    public List<JobTriggerBundle> fireTriggers(List<OperableTrigger> triggers) {
        synchronized (lock) {
            List<JobTriggerBundle> result = new ArrayList<JobTriggerBundle>();
            for (OperableTrigger trigger : triggers) {
                if (!triggerWrappersByKey.containsKey(trigger.getKey())) {
                    continue;
                }
                TriggerWrapper tw = triggerWrappersByKey.get(trigger.getKey());
                if (tw.state != TriggerWrapper.STATE_ACQUIRED) {
                    continue;
                }
                tw.state = TriggerWrapper.STATE_WAITING;
                tw.trigger.fireTrigger();
                JobWrapper jw = jobWrappersByKey.get(tw.trigger.getJobKey());
                if (tw.trigger.getNextFireTime() != null) {
                    if (!jw.jobDetail.isDisallowConcurrentExecute()) {
                        acquiredTriggers.add(tw);
                    }
                }
                JobTriggerBundle jobTriggerBundle = new JobTriggerBundle(jw, tw);
                result.add(jobTriggerBundle);
            }
            return result;
        }
    }

    @Override
    public void completeTrigger(OperableTrigger trigger, Trigger.ExecutionState executionState) {
        synchronized (lock) {
            TriggerWrapper tw = triggerWrappersByKey.get(trigger.getKey());
            if (tw != null) {
                if (executionState == Trigger.ExecutionState.SET_TRIGGER_COMPLETE) {
                    tw.state = TriggerWrapper.STATE_COMPLETE;
                    acquiredTriggers.remove(tw);
                    schedulerThread.signalScheduleChange();
                } else if (executionState == Trigger.ExecutionState.SET_SHELL_RUN_ERROR) {
                    tw.state = TriggerWrapper.STATE_ERROR;
                    schedulerThread.signalScheduleChange();
                } else if (executionState == Trigger.ExecutionState.SET_SHELL_CREATE_ERROR) {
                    setAllTriggersStateError(trigger.getJobKey());
                    schedulerThread.signalScheduleChange();
                }
            }
        }
    }

    protected boolean applyMisfire(OperableTrigger trigger) {
        long misfireTime = System.currentTimeMillis();

        if (misfireThreshold > 0) {
            misfireTime -= misfireThreshold;
        }

        Date tnft = trigger.getNextFireTime();
        if (tnft == null || tnft.getTime() > misfireTime || trigger.getMisfireInstruction() == Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY) {
            return false;
        }
        trigger.updateAfterMisfire();
        return true;
    }

    protected void setAllTriggersStateError(JobKey jobKey) {
        synchronized (lock) {
            List<TriggerWrapper> tws = triggerWrappersByJob.get(jobKey);
            for (TriggerWrapper tw : tws) {
                tw.state = TriggerWrapper.STATE_ERROR;
                acquiredTriggers.remove(tw);
            }
        }
    }
}


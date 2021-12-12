package com.pipiou.scheduler.core;

import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.exception.SchedulerException;
import com.pipiou.scheduler.spi.OperableTrigger;

import java.util.Date;

public class Scheduler {

    private SchedulerResources resources;

    private SchedulerThread thread;

    private volatile boolean shut = false;

    public Scheduler(SchedulerResources resources) {
        this.resources = resources;
        this.thread = new SchedulerThread(this, resources);
        ThreadExecutor threadExecutor = new ThreadExecutor();
        threadExecutor.execute(this.thread);
    }

    public void start() {
        thread.togglePause(false);
    }

    public void shutdown() {
        if (shut) {
            return;
        }
        shut = true;
        thread.halt();
        resources.getThreadPool().shutdown();
    }

    public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        if (jobDetail == null) {
            throw new SchedulerException("JobDetail cannot be null");
        }
        if (trigger == null) {
            throw new SchedulerException("Trigger cannot be null");
        }
        if (jobDetail.getKey() == null) {
            throw new SchedulerException("Job's key cannot be null");
        }
        if (jobDetail.getJobClass() == null) {
            throw new SchedulerException("Job's class cannot be null");
        }
        OperableTrigger trig = (OperableTrigger) trigger;
        if (trigger.getJobKey() == null) {
            trig.setJobKey(jobDetail.getKey());
        } else if (!trigger.getJobKey().equals(jobDetail.getKey())) {
            throw new SchedulerException("Trigger's jobKey does not equals Job's key!");
        }
        Date ft = trig.computeFirstFireTime();
        if (ft == null) {
            throw new SchedulerException("Trigger's firstFireTime cannot be null");
        }

        resources.getJobStore().storeJobAndTrigger(jobDetail, trig);
        thread.signalScheduleChange();
        return ft;
    }
}

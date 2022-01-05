package com.pipiou.scheduler.core;

import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.JobKey;
import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.TriggerKey;
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
        signalScheduleChange();
        return ft;
    }

    public Date scheduleJob(Trigger trigger) throws SchedulerException {
        if (trigger == null) {
            throw new SchedulerException("Trigger cannot be null");
        }
        if (trigger.getJobKey() == null) {
            throw new SchedulerException("Trigger's jobKey cannot be null");
        }
        OperableTrigger trig = (OperableTrigger) trigger;
        Date ft = trig.computeFirstFireTime();
        if (ft == null) {
            throw new SchedulerException("Trigger's firstFireTime cannot be null");
        }

        resources.getJobStore().storeTrigger(trig, false);
        signalScheduleChange();
        return ft;
    }


    public void pauseTrigger(TriggerKey triggerKey) throws SchedulerException {
        resources.getJobStore().pauseTrigger(triggerKey);
        signalScheduleChange();
    }

    public Integer getTriggerState(TriggerKey triggerKey) {
        return resources.getJobStore().getTriggerState(triggerKey);
    }

    public boolean unscheduleJob(TriggerKey triggerKey) throws SchedulerException {
        if (resources.getJobStore().removeTrigger(triggerKey)) {
            signalScheduleChange();
        } else {
            return false;
        }
        return true;
    }

    public boolean deleteJob(JobKey jobKey) throws SchedulerException {
        boolean result;
        result = resources.getJobStore().removeJob(jobKey);
        if (result) {
            signalScheduleChange();
        }
        return result;
    }

    public void notifyJobStoreJobComplete(OperableTrigger trigger, Trigger.ExecutionState executionState) {
        resources.getJobStore().completeTrigger(trigger, executionState);
    }

    public void signalScheduleChange() {
        thread.signalScheduleChange();
    }

    public String getSchedulerName() {
        return resources.getName();
    }

}

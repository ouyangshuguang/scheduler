package com.pipiou.scheduler.core;

import com.pipiou.scheduler.Job;
import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.core.runShell.CyclicRunShell;
import com.pipiou.scheduler.core.runShell.DefaultRunShell;
import com.pipiou.scheduler.exception.SchedulerException;
import com.pipiou.scheduler.impl.trigger.CyclicTrigger;
import com.pipiou.scheduler.simpl.JobTriggerBundle;
import com.pipiou.scheduler.spi.OperableTrigger;

public class RunShellFactory {

    public RunShell createJobRunShell(Scheduler scheduler, JobTriggerBundle bundle) throws SchedulerException {
        RunShell runShell;
        JobExecutionContext jec;
        JobDetail jobDetail = bundle.getJobWrapper().jobDetail;
        OperableTrigger trigger = bundle.getTriggerWrapper().trigger;

        Job jobInstance = jobDetail.getJobInstance();
        if (jobInstance != null) {
            jec = new JobExecutionContext(scheduler, bundle, jobInstance);
        } else {
            Class<? extends Job> jobClass = jobDetail.getJobClass();
            if (jobClass == null) {
                throw new SchedulerException();
            } else {
                Job job = null;
                try {
                    job = jobClass.newInstance();
                    jec = new JobExecutionContext(scheduler, bundle, job);
                } catch (Exception e) {
                    throw new SchedulerException(e);
                }
            }
        }
        if (trigger instanceof CyclicTrigger) {
            runShell = new CyclicRunShell(jec);
        } else {
            runShell = new DefaultRunShell(jec);
        }
        return runShell;
    }

}

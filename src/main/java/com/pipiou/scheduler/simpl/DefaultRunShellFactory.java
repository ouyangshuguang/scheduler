package com.pipiou.scheduler.simpl;

import com.pipiou.scheduler.Job;
import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.core.JobExecutionContext;
import com.pipiou.scheduler.core.RunShell;
import com.pipiou.scheduler.core.Scheduler;
import com.pipiou.scheduler.core.runShell.DefaultRunShell;
import com.pipiou.scheduler.exception.SchedulerException;
import com.pipiou.scheduler.spi.RunShellFactory;

public class DefaultRunShellFactory implements RunShellFactory {

    @Override
    public RunShell createJobRunShell(Scheduler scheduler, JobTriggerBundle bundle) throws SchedulerException {
        RunShell runShell;
        JobExecutionContext jec;
        JobDetail jobDetail = bundle.getJobWrapper().jobDetail;
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
        runShell = new DefaultRunShell(jec);
        return runShell;
    }

}

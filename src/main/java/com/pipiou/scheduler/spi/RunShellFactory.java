package com.pipiou.scheduler.spi;

import com.pipiou.scheduler.core.RunShell;
import com.pipiou.scheduler.core.Scheduler;
import com.pipiou.scheduler.exception.SchedulerException;
import com.pipiou.scheduler.simpl.JobTriggerBundle;

public interface RunShellFactory {

    RunShell createJobRunShell(Scheduler scheduler, JobTriggerBundle bundle) throws SchedulerException;

}

package com.pipiou.scheduler;

import com.pipiou.scheduler.core.JobExecutionContext;

public interface Job {

    void execute(JobExecutionContext jec);

}

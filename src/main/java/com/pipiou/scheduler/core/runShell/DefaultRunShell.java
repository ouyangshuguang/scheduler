package com.pipiou.scheduler.core.runShell;

import com.pipiou.scheduler.core.JobExecutionContext;
import com.pipiou.scheduler.core.RunShell;

public class DefaultRunShell implements RunShell {

    private JobExecutionContext jec = null;

    public DefaultRunShell(JobExecutionContext jec) {
        this.jec = jec;
    }

    @Override
    public void run() {
        jec.getJob().execute();
    }

}

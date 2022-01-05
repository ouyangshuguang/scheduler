package com.pipiou.scheduler.core.runShell;

import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.core.JobExecutionContext;
import com.pipiou.scheduler.core.RunShell;
import com.pipiou.scheduler.core.Scheduler;
import com.pipiou.scheduler.spi.OperableTrigger;

public class DefaultRunShell implements RunShell {

    private JobExecutionContext jec;

    private Scheduler scheduler;

    public DefaultRunShell(JobExecutionContext jec, Scheduler scheduler) {
        this.jec = jec;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        OperableTrigger trigger = (OperableTrigger) jec.getTrigger();
        jec.getJob().execute(jec);
        scheduler.notifyJobStoreJobComplete(trigger, Trigger.ExecutionState.NOOP);
    }

}

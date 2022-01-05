package com.pipiou.scheduler.core.runShell;

import com.pipiou.scheduler.core.JobExecutionContext;
import com.pipiou.scheduler.core.RunShell;
import com.pipiou.scheduler.core.Scheduler;
import com.pipiou.scheduler.impl.trigger.CyclicTrigger;
import com.pipiou.scheduler.simpl.TriggerWrapper;

public class CyclicRunShell implements RunShell {

    private JobExecutionContext jec;

    private Scheduler scheduler;

    public CyclicRunShell(JobExecutionContext jec, Scheduler scheduler) {
        this.jec = jec;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        CyclicTrigger trigger = (CyclicTrigger) jec.getTrigger();
        boolean cyclicFlg = isCyclicFlg(scheduler.getTriggerState(trigger.getKey()));
        while (cyclicFlg) {
            jec.getJob().execute(jec);
            try {
                Thread.sleep(trigger.getSleepTime());
            } catch (InterruptedException e) {
                break;
            }
            cyclicFlg = isCyclicFlg(scheduler.getTriggerState(trigger.getKey()));
        }
    }

    private boolean isCyclicFlg(Integer twState) {
        if (twState == null || twState == TriggerWrapper.STATE_COMPLETE || twState == TriggerWrapper.STATE_ERROR) {
            return false;
        } else {
            return true;
        }
    }

}

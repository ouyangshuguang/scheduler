package com.pipiou.scheduler.core.runShell;

import com.pipiou.scheduler.core.JobExecutionContext;
import com.pipiou.scheduler.core.RunShell;
import com.pipiou.scheduler.impl.trigger.CyclicTrigger;
import com.pipiou.scheduler.simpl.TriggerWrapper;

public class CyclicRunShell implements RunShell {

    private JobExecutionContext jec;

    public CyclicRunShell(JobExecutionContext jec) {
        this.jec = jec;
    }

    @Override
    public void run() {
        TriggerWrapper tw = jec.getTriggerWrapper();
        CyclicTrigger trigger = (CyclicTrigger) jec.getTrigger();
        boolean cyclicFlg = isCyclicFlg(tw.state);
        while (cyclicFlg) {
            jec.getJob().execute();
            try {
                Thread.sleep(trigger.getSleepTime());
            } catch (InterruptedException e) {
                break;
            }
            cyclicFlg = isCyclicFlg(tw.state);
        }
    }

    private boolean isCyclicFlg(int twState) {
        if (twState == TriggerWrapper.STATE_COMPLETE
                || twState == TriggerWrapper.STATE_PAUSED
                || twState == TriggerWrapper.STATE_ERROR) {
            return false;
        } else {
            return true;
        }
    }

}

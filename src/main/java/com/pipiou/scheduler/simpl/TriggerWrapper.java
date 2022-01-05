package com.pipiou.scheduler.simpl;

import com.pipiou.scheduler.JobKey;
import com.pipiou.scheduler.TriggerKey;
import com.pipiou.scheduler.spi.OperableTrigger;

public class TriggerWrapper {

    public TriggerKey key;

    public JobKey jobKey;

    public OperableTrigger trigger;

    public int state = STATE_WAITING;

    public static final int STATE_WAITING = 0;

    public static final int STATE_ACQUIRED = 1;

    public static final int STATE_EXECUTING = 2;

    public static final int STATE_COMPLETE = 3;

    public static final int STATE_PAUSED = 4;

    public static final int STATE_BLOCKED = 5;

    public static final int STATE_PAUSED_BLOCKED = 6;

    public static final int STATE_ERROR = 7;

    public TriggerWrapper(OperableTrigger trigger) {
        this.trigger = trigger;
        this.key = trigger.getKey();
        this.jobKey = trigger.getJobKey();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TriggerWrapper) {
            TriggerWrapper tw = (TriggerWrapper) obj;
            if (tw.key.equals(this.key)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}

package com.pipiou.scheduler.impl;

import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.TriggerFactory;
import com.pipiou.scheduler.impl.trigger.SimpleTrigger;

public class SimpleTriggerFactory implements TriggerFactory {

    private long interval = 0L;

    private int repeatCount = 0;

    private int misfireInstruction = SimpleTrigger.MISFIRE_INSTRUCTION_SMART_POLICY;

    public static SimpleTriggerFactory init() {
        return new SimpleTriggerFactory();
    }

    public SimpleTriggerFactory withIntervalInMilliseconds(long intervalInMillis) {
        this.interval = intervalInMillis;
        return this;
    }

    public SimpleTriggerFactory withRepeatCount(int triggerRepeatCount) {
        this.repeatCount = triggerRepeatCount;
        return this;
    }

    public SimpleTriggerFactory repeatForever() {
        this.repeatCount = SimpleTrigger.REPEAT_FOREVER;
        return this;
    }

    @Override
    public Trigger create() {
        SimpleTrigger st = new SimpleTrigger();
        st.setRepeatInterval(interval);
        st.setRepeatCount(repeatCount);
        st.setMisfireInstruction(misfireInstruction);
        return st;
    }

}

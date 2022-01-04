package com.pipiou.scheduler.impl;

import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.TriggerFactory;
import com.pipiou.scheduler.impl.trigger.DefaultTrigger;

public class DefaultTriggerFactory implements TriggerFactory {

    private long interval = 0L;

    private int repeatCount = 0;

    private int misfireInstruction = DefaultTrigger.MISFIRE_INSTRUCTION_SMART_POLICY;

    public static DefaultTriggerFactory init() {
        return new DefaultTriggerFactory();
    }

    public DefaultTriggerFactory withIntervalInMilliseconds(long intervalInMillis) {
        this.interval = intervalInMillis;
        return this;
    }

    public DefaultTriggerFactory withRepeatCount(int triggerRepeatCount) {
        this.repeatCount = triggerRepeatCount;
        return this;
    }

    public DefaultTriggerFactory repeatForever() {
        this.repeatCount = DefaultTrigger.REPEAT_FOREVER;
        return this;
    }

    @Override
    public Trigger create() {
        DefaultTrigger st = new DefaultTrigger();
        st.setRepeatInterval(interval);
        st.setRepeatCount(repeatCount);
        st.setMisfireInstruction(misfireInstruction);
        return st;
    }

}

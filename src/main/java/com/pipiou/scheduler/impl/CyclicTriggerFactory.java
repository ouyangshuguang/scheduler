package com.pipiou.scheduler.impl;

import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.TriggerFactory;
import com.pipiou.scheduler.impl.trigger.CyclicTrigger;

public class CyclicTriggerFactory implements TriggerFactory {

    private long sleepTime = 100L;

    public static CyclicTriggerFactory init() {
        return new CyclicTriggerFactory();
    }

    public CyclicTriggerFactory withSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    @Override
    public Trigger create() {
        CyclicTrigger ct = new CyclicTrigger();
        ct.setSleepTime(sleepTime);
        return ct;
    }

}

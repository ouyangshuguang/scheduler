package com.pipiou.scheduler.impl.trigger;

import java.util.Date;

public class CyclicTrigger extends AbstractTrigger {

    private static long DEFAULT_SLEEP_TIME = 100L;

    private long sleepTime = DEFAULT_SLEEP_TIME;

    @Override
    public void fireTrigger() {
        previousFireTime = nextFireTime;
        nextFireTime = null;
    }

    @Override
    public Date computeFirstFireTime() {
        return getStartTime();
    }

    @Override
    public void updateAfterMisfire() {

    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }
}

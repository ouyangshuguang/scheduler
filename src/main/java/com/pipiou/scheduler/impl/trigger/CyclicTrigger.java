package com.pipiou.scheduler.impl.trigger;

import com.pipiou.scheduler.Trigger;

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
        nextFireTime = getStartTime();
        return nextFireTime;
    }

    @Override
    public void updateAfterMisfire() {
        int instr = getMisfireInstruction();
        if(instr == Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY) {
            return;
        }

        if (instr == Trigger.MISFIRE_INSTRUCTION_SMART_POLICY) {
            Date newFireTime = new Date();
            setStartTime(newFireTime);
            setNextFireTime(newFireTime);
        }
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }
}

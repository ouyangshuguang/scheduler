package com.pipiou.scheduler.impl.trigger;

import com.pipiou.scheduler.Trigger;

import java.util.Date;

public class DefaultTrigger extends AbstractTrigger {

    private int repeatCount = 0;

    private long repeatInterval = 0;

    private int timesTriggered = 0;

    public static final int REPEAT_FOREVER = -1;

    @Override
    public void fireTrigger() {
        timesTriggered++;
        previousFireTime = nextFireTime;
        nextFireTime = getAfterFireTime(nextFireTime);
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

    private Date getAfterFireTime(Date afterTime) {
        if ((timesTriggered > repeatCount) && (repeatCount != REPEAT_FOREVER)) {
            return null;
        }
        if (afterTime == null) {
            afterTime = new Date();
        }

        if (repeatCount == 0 && afterTime.compareTo(getStartTime()) >= 0) {
            return null;
        }

        long startMillis = getStartTime().getTime();
        long afterMillis = afterTime.getTime();
        long endMillis = (getEndTime() == null) ? Long.MAX_VALUE : getEndTime().getTime();
        if (endMillis <= afterMillis) {
            return null;
        }
        if (afterMillis < startMillis) {
            return new Date(startMillis);
        }
        long numberOfTimesExecuted = ((afterMillis - startMillis) / repeatInterval) + 1;
        if ((numberOfTimesExecuted > repeatCount) && (repeatCount != REPEAT_FOREVER)) {
            return null;
        }

        Date time = new Date(startMillis + (numberOfTimesExecuted * repeatInterval));

        if (endMillis <= time.getTime()) {
            return null;
        }

        return time;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public int getTimesTriggered() {
        return timesTriggered;
    }

    public void setTimesTriggered(int timesTriggered) {
        this.timesTriggered = timesTriggered;
    }

}

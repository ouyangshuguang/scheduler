package com.pipiou.scheduler.impl.trigger;

import com.pipiou.scheduler.JobKey;
import com.pipiou.scheduler.TriggerKey;
import com.pipiou.scheduler.spi.OperableTrigger;

import java.util.Date;
import java.util.Map;

public abstract class AbstractTrigger implements OperableTrigger {

    protected TriggerKey key;

    protected JobKey jobKey;

    protected Map<String, Object> jobDataMap;

    protected Date startTime;

    protected Date endTime;

    protected Date nextFireTime;

    protected Date previousFireTime;

    protected int misfireInstruction;

    @Override
    public TriggerKey getKey() {
        return key;
    }

    @Override
    public void setKey(TriggerKey key) {
        this.key = key;
    }

    @Override
    public JobKey getJobKey() {
        return jobKey;
    }

    @Override
    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    @Override
    public Map<String, Object> getJobDataMap() {
        return jobDataMap;
    }

    @Override
    public void setJobDataMap(Map<String, Object> jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public Date getNextFireTime() {
        return nextFireTime;
    }

    @Override
    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    @Override
    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    @Override
    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    @Override
    public int getMisfireInstruction() {
        return misfireInstruction;
    }

    @Override
    public void setMisfireInstruction(int misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }

}

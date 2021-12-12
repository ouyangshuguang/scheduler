package com.pipiou.scheduler;


import com.pipiou.scheduler.impl.SimpleTriggerFactory;
import com.pipiou.scheduler.spi.MutableTrigger;
import com.pipiou.scheduler.utils.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TriggerBuilder {
    private TriggerKey key;
    private JobKey jobKey;
    private Map<String, Object> jobDataMap = new HashMap<String, Object>();
    private Date startTime = new Date();
    private Date endTime;
    private TriggerFactory triggerFactory;

    public static TriggerBuilder newTrigger() {
        return new TriggerBuilder();
    }

    public TriggerBuilder withIdentity(String name) {
        this.key = new TriggerKey(name);
        return this;
    }

    public TriggerBuilder usingJobData(String dataKey, Object value) {
        this.jobDataMap.put(dataKey, value);
        return this;
    }

    public TriggerBuilder usingJobData(Map<String, Object> jobDataMap) {
        this.jobDataMap.putAll(jobDataMap);
        return this;
    }

    public TriggerBuilder startNow() {
        this.startTime = new Date();
        return this;
    }

    public TriggerBuilder endAt(Date triggerEndTime) {
        this.endTime = triggerEndTime;
        return this;
    }

    public TriggerBuilder withTriggerFactory(TriggerFactory factory) {
        this.triggerFactory = factory;
        return this;
    }

    public Trigger build() {
        if (triggerFactory == null){
            triggerFactory = SimpleTriggerFactory.init();
        }
        MutableTrigger trig = (MutableTrigger) triggerFactory.create();
        trig.setStartTime(startTime);
        trig.setEndTime(endTime);
        if (key == null) {
            key = new TriggerKey(Key.createUniqueName());
        }
        trig.setKey(key);
        if (jobKey != null) {
            trig.setJobKey(jobKey);
        }

        if (!jobDataMap.isEmpty()) {
            trig.setJobDataMap(jobDataMap);
        }
        return trig;
    }
}

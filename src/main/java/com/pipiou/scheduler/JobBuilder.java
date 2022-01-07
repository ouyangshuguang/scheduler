package com.pipiou.scheduler;

import com.pipiou.scheduler.impl.JobDetailImpl;

import java.util.HashMap;
import java.util.Map;

public class JobBuilder {
    private JobKey key;
    private Class<? extends Job> jobClass;
    private Map<String, Object> jobDataMap = new HashMap<String, Object>();

    public static JobBuilder newJob(Class <? extends Job> jobClass) {
        JobBuilder b = new JobBuilder();
        b.jobClass = jobClass;
        return b;
    }

    public JobBuilder withIdentity(String name) {
        this.key = new JobKey(name);
        return this;
    }

    public JobBuilder usingJobData(String dataKey, Object value) {
        this.jobDataMap.put(dataKey, value);
        return this;
    }

    public JobBuilder usingJobData(Map<String, Object> jobDataMap) {
        this.jobDataMap.putAll(jobDataMap);
        return this;
    }

    public JobDetail build() {
        JobDetailImpl job = new JobDetailImpl();
        job.setJobClass(jobClass);
        job.setKey(key);
        job.setJobDataMap(jobDataMap);
        return job;
    }

}

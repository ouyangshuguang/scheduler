package com.pipiou.scheduler.impl;

import com.pipiou.scheduler.DisallowConcurrentExecute;
import com.pipiou.scheduler.Job;
import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.JobKey;
import com.pipiou.scheduler.utils.ClassUtils;

import java.util.Map;

public class JobDetailImpl implements JobDetail {

    private JobKey jobKey;

    private Class<? extends Job> jobClass;

    private Job jobInstance;

    private Map<String, Object> jobDataMap;

    @Override
    public JobKey getKey() {
        return this.jobKey;
    }

    @Override
    public Class<? extends Job> getJobClass() {
        return this.jobClass;
    }

    @Override
    public Job getJobInstance() {
        return jobInstance;
    }

    @Override
    public Map<String, Object> getJobDataMap() {
        return this.jobDataMap;
    }

    @Override
    public boolean isDisallowConcurrentExecute() {
        return ClassUtils.isAnnotationPresent(jobClass, DisallowConcurrentExecute.class);
    }

    public void setKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public void setJobClass(Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }

    public void setJobInstance(Job jobInstance) {
        this.jobInstance = jobInstance;
    }

    public void setJobDataMap(Map<String, Object> jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

}

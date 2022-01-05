package com.pipiou.scheduler.core;

import com.pipiou.scheduler.Job;
import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.simpl.JobTriggerBundle;

import java.util.HashMap;
import java.util.Map;

public class JobExecutionContext {

    private JobDetail jobDetail;

    private Trigger trigger;

    private Map<String, Object> jobDataMap;

    private Job job;

    public JobExecutionContext(JobTriggerBundle jobTriggerBundle, Job job) {
        this.jobDetail = jobTriggerBundle.getJobWrapper().jobDetail;
        this.trigger = jobTriggerBundle.getTriggerWrapper().trigger;
        this.job = job;
        this.jobDataMap = new HashMap<String, Object>();
        if (jobDetail.getJobDataMap() != null) {
            this.jobDataMap.putAll(jobDetail.getJobDataMap());
        }
        if (trigger.getJobDataMap() != null) {
            this.jobDataMap.putAll(trigger.getJobDataMap());
        }
    }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public Map<String, Object> getJobDataMap() {
        return jobDataMap;
    }

    public Job getJob() {
        return job;
    }
}

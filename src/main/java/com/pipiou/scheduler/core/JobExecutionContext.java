package com.pipiou.scheduler.core;

import com.pipiou.scheduler.Job;
import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.simpl.JobTriggerBundle;
import com.pipiou.scheduler.simpl.TriggerWrapper;

import java.util.HashMap;
import java.util.Map;

public class JobExecutionContext {

    private Scheduler scheduler;

    private TriggerWrapper triggerWrapper;

    private JobDetail jobDetail;

    private Trigger trigger;

    private Map<String, Object> jobDataMap;

    private Job job;

    public JobExecutionContext(Scheduler scheduler, JobTriggerBundle jobTriggerBundle, Job job) {
        this.scheduler = scheduler;
        this.triggerWrapper = jobTriggerBundle.getTriggerWrapper();
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

    public Scheduler getScheduler() {
        return scheduler;
    }

    public TriggerWrapper getTriggerWrapper() {
        return triggerWrapper;
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

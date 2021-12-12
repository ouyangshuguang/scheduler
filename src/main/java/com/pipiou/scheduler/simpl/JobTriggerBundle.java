package com.pipiou.scheduler.simpl;

public class JobTriggerBundle {

    private JobWrapper jobWrapper;

    private TriggerWrapper triggerWrapper;

    public JobTriggerBundle(JobWrapper jobWrapper, TriggerWrapper triggerWrapper) {
        this.jobWrapper = jobWrapper;
        this.triggerWrapper = triggerWrapper;
    }

    public JobWrapper getJobWrapper() {
        return jobWrapper;
    }

    public TriggerWrapper getTriggerWrapper() {
        return triggerWrapper;
    }

}

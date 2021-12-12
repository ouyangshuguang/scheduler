package com.pipiou.scheduler.simpl;

import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.JobKey;

public class JobWrapper {

    public JobKey key;

    public JobDetail jobDetail;

    public JobWrapper(JobDetail jobDetail) {
        this.jobDetail = jobDetail;
        this.key = jobDetail.getKey();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JobWrapper) {
            JobWrapper jw = (JobWrapper) obj;
            if (jw.key.equals(this.key)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

}

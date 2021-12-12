package com.pipiou.scheduler.exception;

import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.Trigger;

public class ObjectAlreadyExistsException extends SchedulerException {

    public ObjectAlreadyExistsException(JobDetail jobDetail) {
        super("Unable to store Job : '" + jobDetail.getKey()
                + "', because one already exists with this identification.");
    }

    public ObjectAlreadyExistsException(Trigger trigger) {
        super("Unable to store Trigger : '" + trigger.getKey()
                + "', because one already exists with this identification.");
    }

}

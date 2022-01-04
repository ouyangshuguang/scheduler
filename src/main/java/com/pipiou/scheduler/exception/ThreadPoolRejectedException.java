package com.pipiou.scheduler.exception;

public class ThreadPoolRejectedException extends SchedulerException {

    public ThreadPoolRejectedException(Throwable cause) {
        super(cause);
    }

}

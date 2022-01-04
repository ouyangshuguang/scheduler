package com.pipiou.scheduler.spi;

import com.pipiou.scheduler.exception.SchedulerException;
import com.pipiou.scheduler.exception.ThreadPoolRejectedException;

public interface ThreadPool {

    boolean runInThread(Runnable runnable) throws ThreadPoolRejectedException;

    void shutdown();

    void initialize() throws SchedulerException;
}

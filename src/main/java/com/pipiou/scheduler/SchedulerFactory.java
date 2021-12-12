package com.pipiou.scheduler;

import com.pipiou.scheduler.core.Scheduler;
import com.pipiou.scheduler.exception.SchedulerException;

public interface SchedulerFactory {

    Scheduler getScheduler() throws SchedulerException;

}

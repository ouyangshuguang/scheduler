package com.pipiou.scheduler.impl;

import com.pipiou.scheduler.SchedulerFactory;
import com.pipiou.scheduler.core.Scheduler;
import com.pipiou.scheduler.core.SchedulerResources;
import com.pipiou.scheduler.exception.SchedulerException;
import com.pipiou.scheduler.simpl.DefaultRunShellFactory;
import com.pipiou.scheduler.simpl.RAMJobStore;
import com.pipiou.scheduler.simpl.SimpleThreadPool;

public class DefaultSchedulerFactory implements SchedulerFactory {

    @Override
    public Scheduler getScheduler() throws SchedulerException {
        SchedulerResources resources = new SchedulerResources();
        resources.setJobStore(new RAMJobStore());
        resources.setRunShellFactory(new DefaultRunShellFactory());
        resources.setThreadPool(new SimpleThreadPool());
        Scheduler scheduler = new Scheduler(resources);
        return scheduler;
    }

}

package com.pipiou.scheduler.impl;

import com.pipiou.scheduler.core.Scheduler;
import com.pipiou.scheduler.exception.SchedulerException;

import java.util.HashMap;

public class SchedulerRepository {
    private HashMap<String, Scheduler> schedulers;

    private static SchedulerRepository inst;

    private SchedulerRepository() {
        schedulers = new HashMap<String, Scheduler>();
    }

    public static synchronized SchedulerRepository getInstance() {
        if (inst == null) {
            inst = new SchedulerRepository();
        }

        return inst;
    }

    public synchronized void put(Scheduler scheduler) throws SchedulerException {
        if (schedulers.get(scheduler.getSchedulerName()) != null) {
            throw new SchedulerException("Scheduler with name '" + scheduler.getSchedulerName() + "' already exists.");
        }

        schedulers.put(scheduler.getSchedulerName(), scheduler);
    }

    public synchronized boolean remove(String schedulerName) {
        return (schedulers.remove(schedulerName) != null);
    }

    public synchronized Scheduler get(String schedulerName) {
        return schedulers.get(schedulerName);
    }

}

package com.pipiou.scheduler.core;

import com.pipiou.scheduler.spi.JobStore;
import com.pipiou.scheduler.spi.ThreadPool;

public class SchedulerResources {

    private String name;

    private JobStore jobStore;

    private ThreadPool threadPool;

    private RunShellFactory runShellFactory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobStore getJobStore() {
        return jobStore;
    }

    public void setJobStore(JobStore jobStore) {
        this.jobStore = jobStore;
    }

    public ThreadPool getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    public RunShellFactory getRunShellFactory() {
        return runShellFactory;
    }

    public void setRunShellFactory(RunShellFactory runShellFactory) {
        this.runShellFactory = runShellFactory;
    }
}

package com.pipiou.scheduler.core;

import com.pipiou.scheduler.spi.JobStore;
import com.pipiou.scheduler.spi.RunShellFactory;
import com.pipiou.scheduler.spi.ThreadPool;

public class SchedulerResources {

    private JobStore jobStore;

    private RunShellFactory runShellFactory;

    private ThreadPool threadPool;

    public JobStore getJobStore() {
        return jobStore;
    }

    public void setJobStore(JobStore jobStore) {
        this.jobStore = jobStore;
    }

    public RunShellFactory getRunShellFactory() {
        return runShellFactory;
    }

    public void setRunShellFactory(RunShellFactory runShellFactory) {
        this.runShellFactory = runShellFactory;
    }

    public ThreadPool getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

}

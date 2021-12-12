package com.pipiou.scheduler.spi;

public interface ThreadPool {

    boolean runInThread(Runnable runnable);

    void shutdown();

}

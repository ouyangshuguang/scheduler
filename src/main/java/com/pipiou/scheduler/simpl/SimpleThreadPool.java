package com.pipiou.scheduler.simpl;

import com.pipiou.scheduler.spi.ThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleThreadPool implements ThreadPool {

    private int corePoolSize = 1;

    private int maximumPoolSize = 1;

    private BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>();

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS, workQueue);

    @Override
    public boolean runInThread(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
        return true;
    }

    @Override
    public void shutdown() {
        threadPoolExecutor.shutdownNow();
    }

}

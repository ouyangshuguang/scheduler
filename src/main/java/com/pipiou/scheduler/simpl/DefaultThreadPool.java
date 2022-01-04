package com.pipiou.scheduler.simpl;

import com.pipiou.scheduler.exception.SchedulerException;
import com.pipiou.scheduler.exception.ThreadPoolRejectedException;
import com.pipiou.scheduler.spi.ThreadPool;

import java.util.concurrent.*;

public class DefaultThreadPool implements ThreadPool {

    private int poolSize;

    private BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>();

    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public boolean runInThread(Runnable runnable) throws ThreadPoolRejectedException {
        try {
            threadPoolExecutor.execute(runnable);
        } catch (RejectedExecutionException e) {
            throw new ThreadPoolRejectedException(e.getCause());
        }
        return true;
    }

    @Override
    public void shutdown() {
        threadPoolExecutor.shutdownNow();
    }

    @Override
    public void initialize() throws SchedulerException {
        if (poolSize <= 0) {
            throw new SchedulerException("Thread poolSize must be > 0");
        }
        threadPoolExecutor = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, workQueue);
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

}

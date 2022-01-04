package com.pipiou.scheduler.impl;

import com.pipiou.scheduler.SchedulerFactory;
import com.pipiou.scheduler.core.RunShellFactory;
import com.pipiou.scheduler.core.Scheduler;
import com.pipiou.scheduler.core.SchedulerResources;
import com.pipiou.scheduler.exception.SchedulerException;
import com.pipiou.scheduler.simpl.RAMJobStore;
import com.pipiou.scheduler.simpl.DefaultThreadPool;
import com.pipiou.scheduler.spi.JobStore;

import java.io.*;
import java.util.Properties;

public class DefaultSchedulerFactory implements SchedulerFactory {

    private Properties props = null;

    public static final String DEFAULT_SCHEDULER_NAME = "DefaultScheduler";

    public static final String SCHEDULER_NAME = "scheduler.name";

    public static final String SCHEDULER_JOB_STORE_CLASS = "scheduler.jobStore.class";

    public static final String DEFAULT_SCHEDULER_THREADPOOL_POOLSIZE = "10";

    public static final String SCHEDULER_THREADPOOL_POOLSIZE = "scheduler.threadPool.poolSize";

    public void initialize() throws SchedulerException {
        String requestedFile = System.getProperty("scheduler.properties");
        String propFileName = requestedFile != null ? requestedFile : "scheduler.properties";
        File propFile = new File(propFileName);
        InputStream in = null;
        try {
            if (propFile.exists()) {
                in = new BufferedInputStream(new FileInputStream(propFileName));
                props.load(in);
            } else {
                ClassLoader cl = DefaultSchedulerFactory.class.getClassLoader();
                in = cl.getResourceAsStream("scheduler.properties");
                if (in == null) {
                    in = cl.getResourceAsStream("/scheduler.properties");
                }
                if (in == null) {
                    in = cl.getResourceAsStream("com/pipiou/scheduler/scheduler.properties");
                }
                if (in == null) {
                    throw new SchedulerException("Default scheduler.properties not found in class path");
                }
                props = new Properties();
                props.load(in);
            }
        } catch (Exception e) {
            throw new SchedulerException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) { /* ignore */ }
            }
        }
    }

    @Override
    public Scheduler getScheduler() throws SchedulerException {
        if (props == null) {
            initialize();
        }
        String schedulerName = getProperty(SCHEDULER_NAME, DEFAULT_SCHEDULER_NAME);
        return getScheduler(schedulerName);
    }

    @Override
    public Scheduler getScheduler(String schedulerName) throws SchedulerException {
        if (props == null) {
            initialize();
        }
        SchedulerRepository sr = SchedulerRepository.getInstance();
        Scheduler scheduler = sr.get(schedulerName);
        if (scheduler == null) {
            SchedulerResources resources = new SchedulerResources();
            resources.setName(schedulerName);

            JobStore jobStore;
            String jobStoreClass = getProperty(SCHEDULER_JOB_STORE_CLASS, RAMJobStore.class.getName());
            try {
                jobStore = (JobStore) DefaultSchedulerFactory.class.getClassLoader().loadClass(jobStoreClass).newInstance();
            } catch (Exception e) {
                throw new SchedulerException(e);
            }
            resources.setJobStore(jobStore);

            DefaultThreadPool tp = new DefaultThreadPool();
            String tpPoolSize = getProperty(SCHEDULER_THREADPOOL_POOLSIZE, DEFAULT_SCHEDULER_THREADPOOL_POOLSIZE);
            if (!isNumeric(tpPoolSize)) {
                throw new SchedulerException("config threadPool.poolSize must be a number");
            }
            tp.setPoolSize(Integer.valueOf(tpPoolSize));
            tp.initialize();
            resources.setThreadPool(tp);

            RunShellFactory runShellFactory = new RunShellFactory();
            resources.setRunShellFactory(runShellFactory);
            scheduler = new Scheduler(resources);
            sr.put(scheduler);
        }
        return scheduler;
    }

    public String getProperty(String key, String defaultValue) {
        Object val = props.get(key);
        return (val == null) ? defaultValue : (String) val;
    }

    public static boolean isNumeric(final CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
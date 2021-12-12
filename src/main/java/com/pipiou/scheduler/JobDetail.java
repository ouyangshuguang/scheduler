package com.pipiou.scheduler;

import java.io.Serializable;
import java.util.Map;

public interface JobDetail extends Serializable {

    JobKey getKey();

    Class<? extends Job> getJobClass();

    Job getJobInstance();

    Map<String, Object> getJobDataMap();

    boolean isDisallowConcurrentExecute();
}

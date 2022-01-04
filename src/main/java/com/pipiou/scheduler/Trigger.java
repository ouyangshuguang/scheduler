package com.pipiou.scheduler;

import java.util.Date;
import java.util.Map;

public interface Trigger {

    int MISFIRE_INSTRUCTION_SMART_POLICY = 0;

    int MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY = -1;

    TriggerKey getKey();

    JobKey getJobKey();

    Map<String, Object> getJobDataMap();

    Date getStartTime();

    Date getEndTime();

    Date getNextFireTime();

    Date getPreviousFireTime();

    int getMisfireInstruction();

    enum ExecutionState {NOOP, SET_TRIGGER_COMPLETE, SET_SHELL_CREATE_ERROR, SET_SHELL_RUN_ERROR, SET_SHELL_THREAD_POOL_REJECTED}

}

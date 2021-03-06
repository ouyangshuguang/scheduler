package com.pipiou.scheduler.spi;

import com.pipiou.scheduler.JobDetail;
import com.pipiou.scheduler.JobKey;
import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.TriggerKey;
import com.pipiou.scheduler.core.Scheduler;
import com.pipiou.scheduler.exception.ObjectAlreadyExistsException;
import com.pipiou.scheduler.simpl.JobTriggerBundle;

import java.util.List;

public interface JobStore {

    void initialize(Scheduler scheduler);

    void storeJob(JobDetail jobDetail, boolean replaceExisting) throws ObjectAlreadyExistsException;

    boolean removeJob(JobKey jobKey);

    void storeTrigger(OperableTrigger trigger, boolean replaceExisting) throws ObjectAlreadyExistsException;

    void pauseTrigger(TriggerKey triggerKey);

    boolean removeTrigger(TriggerKey triggerKey);

    Integer getTriggerState(TriggerKey key);

    void storeJobAndTrigger(JobDetail newJob, OperableTrigger newTrigger) throws ObjectAlreadyExistsException;

    List<OperableTrigger> acquireNextTriggers(long noLaterThan);

    void releaseAcquiredTrigger(OperableTrigger trigger);

    List<JobTriggerBundle> fireTriggers(List<OperableTrigger> triggers);

    void completeTrigger(OperableTrigger trigger, Trigger.ExecutionState executionState);

}

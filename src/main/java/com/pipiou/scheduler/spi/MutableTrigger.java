package com.pipiou.scheduler.spi;

import com.pipiou.scheduler.JobKey;
import com.pipiou.scheduler.Trigger;
import com.pipiou.scheduler.TriggerKey;

import java.util.Date;
import java.util.Map;

public interface MutableTrigger extends Trigger {

    void setKey(TriggerKey triggerKey);

    void setJobKey(JobKey jobKey);

    void setJobDataMap(Map<String, Object> jobDataMap);

    void setStartTime(Date startTime);

    void setEndTime(Date endTime);

    void setNextFireTime(Date nextFireTime);

    void setPreviousFireTime(Date previousFireTime);

    void setMisfireInstruction(int misfireInstruction);

}

package com.pipiou.scheduler.spi;

import java.util.Date;

public interface OperableTrigger extends MutableTrigger {
    void fireTrigger();

    Date computeFirstFireTime();

    void updateAfterMisfire();
}

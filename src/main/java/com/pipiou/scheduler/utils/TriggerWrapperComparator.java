package com.pipiou.scheduler.utils;

import com.pipiou.scheduler.TriggerKey;
import com.pipiou.scheduler.simpl.TriggerWrapper;

import java.util.Comparator;
import java.util.Date;

public class TriggerWrapperComparator implements Comparator<TriggerWrapper> {

    public static int compare(Date nextFireTime1, TriggerKey key1, Date nextFireTime2, TriggerKey key2) {
        if (nextFireTime1 != null || nextFireTime2 != null) {
            if (nextFireTime1 == null) {
                return 1;
            }

            if (nextFireTime2 == null) {
                return -1;
            }

            if (nextFireTime1.before(nextFireTime2)) {
                return -1;
            }

            if (nextFireTime1.after(nextFireTime2)) {
                return 1;
            }
        }

        return key1.compareTo(key2);
    }

    @Override
    public int compare(TriggerWrapper t1, TriggerWrapper t2) {
        return compare(t1.trigger.getNextFireTime(), t1.trigger.getKey(), t2.trigger.getNextFireTime(), t2.trigger.getKey());
    }

}

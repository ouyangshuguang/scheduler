package com.pipiou.scheduler.utils;

import java.lang.annotation.Annotation;

public class ClassUtils {

    public static boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> a) {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            if (c.isAnnotationPresent(a))
                return true;
            if(isAnnotationPresentOnInterfaces(c, a))
                return true;
        }
        return false;
    }

    private static boolean isAnnotationPresentOnInterfaces(Class<?> clazz, Class<? extends Annotation> a) {
        for(Class<?> i : clazz.getInterfaces()) {
            if( i.isAnnotationPresent(a) )
                return true;
            if(isAnnotationPresentOnInterfaces(i, a))
                return true;
        }

        return false;
    }

}

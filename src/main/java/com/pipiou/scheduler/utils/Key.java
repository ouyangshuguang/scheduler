package com.pipiou.scheduler.utils;

import java.io.Serializable;
import java.util.UUID;

public class Key implements Serializable, Comparable<Key> {
    private static final long serialVersionUID = 1L;

    private final String name;

    public Key(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Key other = (Key) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Key o) {
        return name.compareTo(o.getName());
    }

    public static String createUniqueName() {
        return UUID.randomUUID().toString();
    }
}

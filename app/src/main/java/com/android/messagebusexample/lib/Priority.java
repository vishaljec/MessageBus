package com.android.messagebusexample.lib;

public enum Priority {
    HIGHER(20),
    HIGH(10),
    NORMAL(0),
    LOW(-10),
    VERIFICATION(-100);

    private final int code;

    Priority(int code) {
        this.code = code;
    }

    public boolean isHigherThan(Priority p) {
        return this.code > p.code;
    }
}

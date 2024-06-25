package com.integrations.orderprocessing.stackenable.enums;

public enum EventStatus {
    FAIL(0), SUCCESS(1);
    private final int value;
    EventStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}

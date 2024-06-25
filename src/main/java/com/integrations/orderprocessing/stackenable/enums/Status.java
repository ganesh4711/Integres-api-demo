package com.integrations.orderprocessing.stackenable.enums;

public enum Status {
    INACTIVE(0), ACTIVE(1);
    private final int value;
    Status(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}

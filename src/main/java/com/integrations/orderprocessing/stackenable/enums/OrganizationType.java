package com.integrations.orderprocessing.stackenable.enums;

public enum OrganizationType{
    Carrier(0),Shipper(1),Warehouse(2);
    private final int value;
    OrganizationType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}

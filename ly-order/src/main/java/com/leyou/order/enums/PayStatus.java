package com.leyou.order.enums;

public enum  PayStatus {

    NOT_PAY(0),
    SUCCESS(1),
    FAIL(2)
    ;
    PayStatus(int value) {
        this.value = value;
    }

    int value;

    public int getValue() {
        return value;
    }
}

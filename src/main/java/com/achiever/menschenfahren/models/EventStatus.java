package com.achiever.menschenfahren.models;

/**
 * CreatedBy : edangol
 * CreatedOn : 10/04/2020
 * Description :
 **/
public enum EventStatus {
    APPROVED("approved"),
    REJECTED("rejected"),
    PENDING("pending"),
    KICKED("kicked");

    private String value;

    EventStatus(String status) {
        this.value = status;
    }

    public String getValue() {
        return value;
    }
}

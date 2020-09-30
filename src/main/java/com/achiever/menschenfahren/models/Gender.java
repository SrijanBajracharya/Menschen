package com.achiever.menschenfahren.models;

/**
 * CreatedBy : edangol
 * CreatedOn : 10/04/2020
 * Description :
 **/
public enum Gender {
    MALE("male"),
    FEMALE("female"),
    OTHERS("others");

    private String value;

    Gender(String gender) {
        this.value = gender;
    }

    public String getValue() {
        return value;
    }
}

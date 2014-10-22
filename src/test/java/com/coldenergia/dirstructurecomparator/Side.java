package com.coldenergia.dirstructurecomparator;

/**
 * User: coldenergia
 * Date: 10/22/14
 * Time: 9:12 PM
 */
public enum Side {

    LEFT("left"),

    RIGHT("right");

    Side(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

}

package com.spring.jwt.laptop.Dropdown.model;

public enum Warranty implements DbEnum{
    NO_WARRANTY("0", "No Warranty"),
    ONE_YEAR("1", "1 Year"),
    TWO_YEAR("2", "2 Years"),
    THREE_YEAR("3", "3 Years");

    private final String dbValue;
    private final String label;

    Warranty(String dbValue, String label) {
        this.dbValue = dbValue;
        this.label = label;
    }

    public static Warranty fromYears(Long warrantyInYear) {
        if (warrantyInYear == null) {
            return NO_WARRANTY;
        }

        for (Warranty w : values()) {
            if (w.dbValue.equals(String.valueOf(warrantyInYear))) {
                return w;
            }
        }

        throw new IllegalArgumentException(
                "Invalid warranty years: " + warrantyInYear
        );
    }

    @Override
    public String getDbValue() {
        return dbValue;
    }

    public String getLabel() {
        return label;
    }

    public static Warranty fromDbValue(String dbValue) {
        for (Warranty w : values()) {
            if (w.dbValue.equalsIgnoreCase(dbValue)) {
                return w;
            }
        }
        throw new IllegalArgumentException("Invalid Warranty value: " + dbValue);
    }
}

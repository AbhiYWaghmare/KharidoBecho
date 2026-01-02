package com.spring.jwt.laptop.Dropdown.model;

public enum ScreenSize implements DbEnum{
    INCH_13("13 inch", "13 inch"),
    INCH_14("14 inch", "14 inch"),
    INCH_15_6("15.6 inch", "15.6 inch"),
    INCH_16("16 inch", "16 inch"),
    INCH_16_5("16.5 inch","16.5 inch"),
    INCH_17("17 inch", "17 inch");

    private final String dbValue;
    private final String label;

    ScreenSize(String dbValue, String label) {
        this.dbValue = dbValue;
        this.label = label;
    }

    @Override
    public String getDbValue() {
        return dbValue;
    }

    public String getLabel() {
        return label;
    }

    public static ScreenSize fromDbValue(String dbValue) {
        for (ScreenSize s : values()) {
            if (s.dbValue.equalsIgnoreCase(dbValue)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid ScreenSize value: " + dbValue);
    }
}

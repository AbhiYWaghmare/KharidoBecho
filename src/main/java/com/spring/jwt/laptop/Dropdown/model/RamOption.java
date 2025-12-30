package com.spring.jwt.laptop.Dropdown.model;


public enum RamOption implements DbEnum{
    RAM_4("4GB", "4GB"),
    RAM_8("8GB", "8GB"),
    RAM_16("16GB", "16GB"),
    RAM_32("32GB", "32GB"),
    RAM_64("64GB", "64GB");

    private final String label;
    private final String dbValue;


    RamOption(String dbValue, String label) {
        this.dbValue = dbValue;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static RamOption fromDbValue(String dbValue) {
        for (RamOption r : values()) {
            if (r.dbValue.equalsIgnoreCase(dbValue)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Invalid RAM value: " + dbValue);
    }

}

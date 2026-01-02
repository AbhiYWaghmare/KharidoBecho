package com.spring.jwt.laptop.Dropdown.model;

public enum StorageOption implements DbEnum{
    SSD_256("256GB SSD", "256GB SSD"),
    SSD_512("512GB SSD", "512GB SSD"),
    SSD_1_TB("1TB SSD", "1TB SSD"),
    HDD_1_TB("1TB HDD", "1TB HDD");

    private final String dbValue;
    private final String label;

    StorageOption(String dbValue, String label) {
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

    public static StorageOption fromDbValue(String dbValue) {
        for (StorageOption s : values()) {
            if (s.dbValue.equalsIgnoreCase(dbValue)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid Storage value: " + dbValue);
    }
}

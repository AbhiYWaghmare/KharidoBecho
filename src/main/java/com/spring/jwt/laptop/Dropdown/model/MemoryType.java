package com.spring.jwt.laptop.Dropdown.model;

public enum MemoryType implements DbEnum{
    DDR3("DDR3", "DDR3"),
    DDR4("DDR4", "DDR4"),
    DDR5("DDR5", "DDR5"),
    LPDDR4("LPDDR4", "LPDDR4"),
    LPDDR5("LPDDR5", "LPDDR5");

    private final String dbValue;
    private final String label;

    MemoryType(String dbValue, String label) {
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

    public static MemoryType fromDbValue(String dbValue) {
        for (MemoryType m : values()) {
            if (m.dbValue.equalsIgnoreCase(dbValue)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Invalid MemoryType value: " + dbValue);
    }
}

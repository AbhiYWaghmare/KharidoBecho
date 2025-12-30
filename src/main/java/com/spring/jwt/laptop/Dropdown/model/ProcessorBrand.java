package com.spring.jwt.laptop.Dropdown.model;

public enum ProcessorBrand implements DbEnum{
    INTEL("INTEL", "Intel"),
    AMD("AMD", "AMD"),
    APPLE("APPLE", "Apple"),
    QUALCOMM("QUALCOMM", "Qualcomm");

    private final String dbValue;
    private final String label;

    ProcessorBrand(String dbValue, String label) {
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

    public static ProcessorBrand fromDbValue(String dbValue) {
        for (ProcessorBrand p : values()) {
            if (p.dbValue.equalsIgnoreCase(dbValue)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid ProcessorBrand value: " + dbValue);
    }
}


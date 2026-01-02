package com.spring.jwt.laptop.Dropdown.model;

public enum GraphicsBrand implements DbEnum{
    INTEL("INTEL", "Intel"),
    NVIDIA("NVIDIA", "NVIDIA"),
    AMD("AMD", "AMD"),
    APPLE("APPLE", "Apple");

    private final String dbValue;
    private final String label;

    GraphicsBrand(String dbValue, String label) {
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

    public static GraphicsBrand fromDbValue(String dbValue) {
        for (GraphicsBrand g : values()) {
            if (g.dbValue.equalsIgnoreCase(dbValue)) {
                return g;
            }
        }
        throw new IllegalArgumentException("Invalid GraphicsBrand value: " + dbValue);
    }
}

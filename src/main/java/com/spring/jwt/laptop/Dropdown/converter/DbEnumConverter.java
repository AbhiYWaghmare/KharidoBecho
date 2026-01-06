package com.spring.jwt.laptop.Dropdown.converter;

import com.spring.jwt.laptop.Dropdown.model.DbEnum;
import jakarta.persistence.AttributeConverter;

public abstract  class DbEnumConverter<E extends Enum<E> & DbEnum> implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    protected DbEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        String normalized = dbData
                .trim()
                .replaceAll("[\\u0000-\\u001F]", "") // remove control chars
                .toUpperCase();

        if (normalized.isBlank()) {
            return null;
        }

        for (E constant : enumClass.getEnumConstants()) {
            if (constant.getDbValue().equalsIgnoreCase(normalized)) {
                return constant;
            }
        }

        // Log but never break entity loading
        System.err.println(
                "⚠ Unknown DB value '" + dbData + "' for enum " + enumClass.getSimpleName()
        );

        return null;
    }
}

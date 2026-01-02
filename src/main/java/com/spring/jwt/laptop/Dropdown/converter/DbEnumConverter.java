package com.spring.jwt.laptop.Dropdown.converter;

import com.spring.jwt.laptop.Dropdown.model.DbEnum;
import jakarta.persistence.AttributeConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract  class DbEnumConverter<E extends Enum<E> & DbEnum> implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        for (E constant : enumClass.getEnumConstants()) {
            if (constant.getDbValue().equalsIgnoreCase(dbData)) {
                return constant;
            }
        }

        throw new IllegalArgumentException(
                "Unknown DB value '" + dbData + "' for enum " + enumClass.getSimpleName()
        );
    }
}

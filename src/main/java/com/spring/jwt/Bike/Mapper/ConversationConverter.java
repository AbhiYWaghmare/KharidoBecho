package com.spring.jwt.Bike.Mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwt.Bike.dto.BookingConversationDto;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter(autoApply = false)
public class ConversationConverter
        implements AttributeConverter<List<BookingConversationDto>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<BookingConversationDto> attribute) {
        try {
            if (attribute == null) return "[]";
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert conversation to JSON", e);
        }
    }

    @Override
    public List<BookingConversationDto> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return new ArrayList<>();
            }
            return mapper.readValue(
                    dbData,
                    new TypeReference<List<BookingConversationDto>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to conversation list", e);
        }
    }
}

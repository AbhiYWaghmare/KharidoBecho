package com.spring.jwt.car.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Converter
public class ConversationConverter implements AttributeConverter<List<Map<String, Object>>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Map<String, Object>> conversationList) {
        try {
            return mapper.writeValueAsString(conversationList);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Override
    public List<Map<String, Object>> convertToEntityAttribute(String json) {
        try {
            if (json == null || json.isEmpty()) return new ArrayList<>();
            return mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}

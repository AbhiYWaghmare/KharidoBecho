/*package com.spring.jwt.Bike.Mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(ObjectMapper mapper) {
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, true);

        // Manually prevent empty string coercion (works even on older Jackson)
        mapper.getCoercionConfigFor(LogicalType.Integer)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.Fail);
        mapper.getCoercionConfigFor(LogicalType.Float)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.Fail);
        mapper.getCoercionConfigFor(LogicalType.Boolean)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.Fail);

        return mapper;
    }
}*/


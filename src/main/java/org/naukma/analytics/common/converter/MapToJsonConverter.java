package org.naukma.analytics.common.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.persistence.AttributeConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class MapToJsonConverter implements AttributeConverter<Map<LocalDate, Integer>, String> {

    private final ObjectMapper objectMapper;

    public MapToJsonConverter() {
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeySerializer(LocalDate.class, new LocalDateKeySerializer());
        module.addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer());
        this.objectMapper.registerModule(module);
    }

    @Override
    public String convertToDatabaseColumn(Map<LocalDate, Integer> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (IOException e) {
            throw new IllegalStateException("Could not convert map to JSON", e);
        }
    }

    @Override
    public Map<LocalDate, Integer> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<Map<LocalDate, Integer>>() {});
        } catch (IOException e) {
            throw new IllegalStateException("Could not convert JSON to map", e);
        }
    }
}

package org.naukma.analytics.common.converter;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateKeyDeserializer extends KeyDeserializer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt){
        return LocalDate.parse(key, FORMATTER);
    }
}



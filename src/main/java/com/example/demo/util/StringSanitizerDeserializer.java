package com.example.demo.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class StringSanitizerDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        if (value == null) {
            return null;
        }
        // 제어 문자(줄바꿈, 탭을 제외한 모든 제어 문자) 제거
        return value.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
    }
}
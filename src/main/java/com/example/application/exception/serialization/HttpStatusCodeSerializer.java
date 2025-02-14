package com.example.application.exception.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class HttpStatusCodeSerializer extends JsonSerializer<HttpStatus> {
    @Override
    public void serialize(HttpStatus value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.value());
    }
}

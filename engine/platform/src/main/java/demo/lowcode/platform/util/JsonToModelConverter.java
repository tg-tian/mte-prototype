package demo.lowcode.platform.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.lowcode.platform.model.device.Model;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter
public class JsonToModelConverter implements AttributeConverter<Model, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Model model) {
        try {
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public Model convertToEntityAttribute(String s) {
        try {
            return s == null ? null : objectMapper.readValue(s, Model.class);
        } catch (IOException e) {
            return null;
        }
    }
}

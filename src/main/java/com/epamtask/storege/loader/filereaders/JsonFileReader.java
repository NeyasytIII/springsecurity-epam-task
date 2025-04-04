package com.epamtask.storege.loader.filereaders;

import com.epamtask.aspect.annotation.Loggable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class JsonFileReader {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Loggable
    public <T> List<T> readFromFile(Resource resource, Class<T> clazz) {
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new IllegalStateException("Error reading file for " + clazz.getSimpleName(), e);
        }
    }
}
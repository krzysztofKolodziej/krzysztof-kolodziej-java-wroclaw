package com.example.krzysztof_kolodziej_java_wroclaw;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@AllArgsConstructor
public class JsonUtils {

    private final ObjectMapper mapper;

    public <T> List<T> readList(String path, TypeReference<List<T>> typeRef) throws Exception {
        return mapper.readValue(new File(path), typeRef);
    }
}
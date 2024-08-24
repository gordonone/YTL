package com.ytl.crm.service.ws.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/3/13 9:32
 */
public class ResourceUtil {

    private ResourceUtil() {
    }

    /**
     * 读取资源文件
     *
     * @param fileName 文件名
     * @param <T>      泛型
     * @param clazz    类型
     * @return List<T>
     * @throws IOException IOException
     */
    public static <T> List<T> readResource(String fileName, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        InputStream inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream(fileName);
        return objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    /**
     * 读取资源文件
     *
     * @param fileName 文件名
     * @param <T>      泛型
     * @return List<T>
     * @throws IOException IOException
     */
    public static <T> List<T> readResponseResource(String fileName, TypeReference<List<T>> typeReference) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        InputStream inputStream = ResourceUtil.class.getClassLoader().getResourceAsStream(fileName);
        return objectMapper.readValue(inputStream, typeReference);
    }

    private static void configureObjectMapper(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                String dateTimeString = jsonParser.getValueAsString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                return LocalDateTime.parse(dateTimeString, formatter);
            }
        });
        module.addDeserializer(LocalTime.class, new JsonDeserializer<LocalTime>() {
            @Override
            public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                String dateTimeString = jsonParser.getValueAsString();
                return LocalTime.parse(dateTimeString);
            }
        });
        objectMapper.registerModule(module);
    }

}

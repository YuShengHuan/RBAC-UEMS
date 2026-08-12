package com.ru.app.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil<T> {

    // 全局单例 ObjectMapper
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 对象转 JSON 字符串
     */
    public static <T> String toJsonString(T data) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(data);
    }

    /**
     * JSON 字符串转对象
     */
    public static <T> T toObject(String json, Class<T> clazz) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }
}

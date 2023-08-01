package com.github.mimiknight.monkey.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.mimiknight.kuca.utils.constant.DateTimeFormatStandard;
import com.github.mimiknight.kuca.utils.exception.JsonConvertException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * JSON工具类
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-08-01 22:27:01
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    static {
        //对象的所有字段全部列入
        MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认Date转换timestamps形式
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss.SSS XXX
        MAPPER.setDateFormat(new SimpleDateFormat(DateTimeFormatStandard.STANDARD_6));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // PrettyPrinter 格式化输出
        MAPPER.configure(SerializationFeature.INDENT_OUTPUT, false);
        // 忽略无法转换的对象
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 指定时区
        MAPPER.setTimeZone(TimeZone.getDefault());
        // 注册java8新时间类型模块
        MAPPER.registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .registerModule(new ParameterNamesModule());
    }

    /**
     * 对象转JSON字符串
     *
     * @param object 对象
     * @return {@link String}
     */
    public static String toJson(Object object) {
        if (null == object) {
            return null;
        }
        try {
            return (object instanceof String) ? (String) object : MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Object convert to JSON Failed.");
            throw new JsonConvertException("Object convert to JSON Failed", e);
        }
    }

    /**
     * JSON字符串转对象
     *
     * @param json  JSON字符串
     * @param clazz 待转对象Class类型
     * @return {@link T}
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON convert to object Failed.");
            throw new JsonConvertException("JSON convert to object Failed", e);
        }
    }

    /**
     * JSON字符串转对象
     *
     * @param json          JSON字符串
     * @param typeReference 引用类型
     * @return {@link T}
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("JSON convert to object Failed.");
            throw new JsonConvertException("JSON convert to object Failed", e);
        }
    }

    /**
     * JSON字符串转JsonNode
     *
     * @param json JSON字符串
     * @return {@link JsonNode}
     */
    public static JsonNode readTree(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("JSON convert to JsonNode Failed.");
            throw new JsonConvertException("JSON convert to JsonNode Failed", e);
        }
    }


    /**
     * 根据字段名从父级JsonNode对象中获取子节点JsonNode对象
     *
     * @param jsonNode  JsonNode对象
     * @param fieldName 字段名
     * @return {@link JsonNode}
     */
    public static JsonNode children(JsonNode jsonNode, String fieldName) {
        if (null == jsonNode || StringUtils.isBlank(fieldName)) {
            return null;
        }
        return jsonNode.get(fieldName);
    }

    /**
     * 根据字段名从JSON字符串中获取子节点JsonNode对象
     *
     * @param json      JSON字符串
     * @param fieldName 字段名
     * @return {@link JsonNode}
     */
    public static JsonNode children(String json, String fieldName) {
        JsonNode parent = readTree(json);
        return children(parent, fieldName);
    }

    /**
     * 创建JSON对象节点
     *
     * @return {@link ObjectNode}
     */
    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

}

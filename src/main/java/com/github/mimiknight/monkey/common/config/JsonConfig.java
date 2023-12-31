package com.github.mimiknight.monkey.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.mimiknight.kuca.utils.constant.DateTimeFormatStandard;
import com.github.mimiknight.kuca.utils.constant.TimeZoneGMT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * JSON配置类
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-07-30 19:53:34
 */
@Configuration
public class JsonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        config(mapper);
        return mapper;
    }

    private void config(ObjectMapper mapper) {
        //对象的所有字段全部列入
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认Date转换timestamps形式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss.SSS XXX
        mapper.setDateFormat(new SimpleDateFormat(DateTimeFormatStandard.STANDARD_6));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // PrettyPrinter 格式化输出
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        // 忽略无法转换的对象
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 指定时区
        mapper.setTimeZone(TimeZone.getTimeZone(ZoneId.of(TimeZoneGMT.GMT)));

        mapper.registerModule(new JavaTimeModule()) // 注册java8新时间类型模块
                .registerModule(new Jdk8Module())
                .registerModule(new ParameterNamesModule());
    }

}

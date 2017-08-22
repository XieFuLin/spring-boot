package com.xfl.boot.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by XFL
 * time on 2017/8/16 22:39
 * description:
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    //    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(customJackson2HttpMessageConverter());
//    }
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(customJackson2HttpMessageConverter());
        super.extendMessageConverters(converters);
    }
//    @Bean
//    @Primary
//    @ConditionalOnMissingBean(ObjectMapper.class)
//    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//
//        /*
//         * 返回的JSON字符串中含有我们并不需要的字段，那么当对应的实体类中不含有该字段时，会抛出一个异常，告诉你有些字段没有在实体类中找到
//         */
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
//
//        // 允许单引号
//        //objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
//        // 字段和值都加引号
//        // objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//        // 数字也加引号
//        objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
//        //objectMapper.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);
//
//        //Null值输出空字符串
//        serializerProvider.setNullValueSerializer(new JsonSerializer<Object>() {
//            @Override
//            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
//                jsonGenerator.writeString("");
//            }
//
//        });
//        return objectMapper;
//    }


}

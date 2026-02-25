package com.vasyerp.rolebasedsystem.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vasyerp.rolebasedsystem.dto.ProductDTO;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.List;

@Configuration
public class RedisCachingConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer(objectMapper)
                        )
                );
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer productsAllReadableCacheCustomizer() {

        ObjectMapper productCacheMapper = new ObjectMapper();
        productCacheMapper.registerModule(new JavaTimeModule());
        productCacheMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        productCacheMapper.enable(SerializationFeature.INDENT_OUTPUT);


        Jackson2JsonRedisSerializer<List<ProductDTO>> productsAllSerializer =
                new Jackson2JsonRedisSerializer<>(
                        productCacheMapper.getTypeFactory()
                                .constructCollectionType(List.class, ProductDTO.class)
                );

        productsAllSerializer.setObjectMapper(productCacheMapper);


        RedisCacheConfiguration productsAllConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10))
                        .disableCachingNullValues()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(productsAllSerializer)
                        );


        return builder -> builder.withCacheConfiguration(
                "productsAll",
                productsAllConfig
        );
    }
}
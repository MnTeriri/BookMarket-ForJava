package com.example.bookmarketfront.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableCaching
public class RedisConfig {
    @Value("${redis.database0}")
    private int database0;
    @Value("${redis.database1}")
    private int database1;
    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.password}")
    private String password;

    public RedisConfig() {
        log.debug("创建配置类对象：RedisConfig");
    }

    @Bean
    public RedisTemplate<String, Object> cacheRedis() {
        return getRedisTemplate(database0);
    }

    @Bean
    public StringRedisTemplate tokenRedis() {
        return getStringRedisTemplate(database1);
    }

    private JedisConnectionFactory getJedisConnectionFactory(int database) {
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(database);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        /*
            非spring注入使用RedisTemplate,需先调用afterPropertiesSet()方法
            不然会报：JedisConnectionFactory was not initialized through afterPropertiesSet()
        */
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    private RedisTemplate<String, Object> getRedisTemplate(int database) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(getJedisConnectionFactory(database));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        /*
            非spring注入使用RedisTemplate,需先调用afterPropertiesSet()方法
            不然会报：JedisConnectionFactory was not initialized through afterPropertiesSet()
        */
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private StringRedisTemplate getStringRedisTemplate(int database) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(getJedisConnectionFactory(database));
        /*
            非spring注入使用RedisTemplate,需先调用afterPropertiesSet()方法
            不然会报：JedisConnectionFactory was not initialized through afterPropertiesSet()
        */
        stringRedisTemplate.afterPropertiesSet();
        return stringRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}

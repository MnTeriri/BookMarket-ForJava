package com.example.bookmarketpassport.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisUtils {
    private static RedisTemplate<String, Object> redisTemplate;

    public static void setCacheObject(String key, Object value) {
        getRedisTemplate();
        redisTemplate.opsForValue().set(key, value);
    }

    public static <T> T getCacheObject(String key) {
        getRedisTemplate();
        ValueOperations<String, Object> operation = redisTemplate.opsForValue();
        return (T) operation.get(key);
    }

    public static boolean deleteObject(final String key) {
        getRedisTemplate();
        return redisTemplate.delete(key);
    }

    private static RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = SpringContextUtils.getBean("redisTemplate");
        }
        return redisTemplate;
    }
}

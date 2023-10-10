package com.example.bookmarketpassport.utils;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisUtils {
    private static StringRedisTemplate stringRedisTemplate;

    public static void setCacheObject(String key, Object value) {
        getStringRedisTemplate();
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }


    public static <T> T getCacheObject(String key, Class<T> clazz) {
        getStringRedisTemplate();
        String json = stringRedisTemplate.opsForValue().get(key);
        return JSON.parseObject(json, clazz);
    }

    public static boolean deleteObject(String key) {
        getStringRedisTemplate();
        return stringRedisTemplate.delete(key);
    }

    private static void getStringRedisTemplate() {
        if (stringRedisTemplate == null) {
            stringRedisTemplate = SpringContextUtils.getBean("stringRedisTemplate");
        }
    }
}

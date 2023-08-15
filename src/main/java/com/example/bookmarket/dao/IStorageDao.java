package com.example.bookmarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarket.config.RedisCache;
import com.example.bookmarket.model.Storage;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace(implementation = RedisCache.class)
public interface IStorageDao extends BaseMapper<Storage> {
}

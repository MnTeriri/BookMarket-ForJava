package com.example.bookmarketfront.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarketfront.config.RedisCache;
import com.example.bookmarketfront.model.Storage;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace(implementation = RedisCache.class)
public interface IStorageDao extends BaseMapper<Storage> {
}

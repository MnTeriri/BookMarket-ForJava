package com.example.bookmarketfront.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarketfront.config.RedisCache;
import com.example.bookmarketfront.model.Address;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace(implementation = RedisCache.class)
public interface IAddressDao extends BaseMapper<Address> {
    @Select("SELECT * FROM Address WHERE id=#{aid}")
    public Address searchById(Integer aid);

    @Update("UPDATE Address SET is_default=0 WHERE uid=#{uid}")
    public boolean updateAddressDefault(String uid);

    @Select("SELECT * FROM Address WHERE uid=#{uid} AND is_default=1")
    public Address searchDefaultAddress(String uid);
}

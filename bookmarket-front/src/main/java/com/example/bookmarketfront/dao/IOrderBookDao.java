package com.example.bookmarketfront.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarketfront.config.RedisCache;
import com.example.bookmarketfront.model.Order;
import com.example.bookmarketfront.model.OrderBook;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheNamespace(implementation = RedisCache.class)
public interface IOrderBookDao extends BaseMapper<OrderBook> {
    @Select("SELECT * FROM Order_Book WHERE oid=#{oid}")
    @Results({
            @Result(property = "bid", column = "bid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "book", column = "bid", one = @One(select = "com.example.bookmarketfront.dao.IBookDao.searchBookByBid"))
    })
    public List<OrderBook> getOrderDetail(String oid);
}

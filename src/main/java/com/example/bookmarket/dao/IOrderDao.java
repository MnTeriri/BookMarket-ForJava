package com.example.bookmarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarket.model.Order;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDao extends BaseMapper<Order> {
    @Select("SELECT COUNT(id) FROM `Order` WHERE oid LIKE CONCAT('%', #{oid}, '%') AND uid=#{uid}")
    public Long getRecordsFiltered(String oid, String uid);

    @Select("SELECT COUNT(id) FROM `Order` WHERE oid LIKE CONCAT('%', #{oid}, '%') AND uid=#{uid} AND status=#{status}")
    public Long getRecordsFilteredByStatus(String oid, String uid, Integer status);
}

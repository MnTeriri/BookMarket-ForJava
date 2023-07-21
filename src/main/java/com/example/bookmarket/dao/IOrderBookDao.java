package com.example.bookmarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarket.model.OrderBook;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderBookDao extends BaseMapper<OrderBook> {
}

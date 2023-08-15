package com.example.bookmarket.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bookmarket.config.SpringContextUtils;
import com.example.bookmarket.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyBatisTest {
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IBookDao bookDao;
    @Test
    void secondCacheTest(){
        orderDao.getOrderList("","123456789","all",1,11);
        //bookDao.searchBookByBid("9787516156526");
    }
}
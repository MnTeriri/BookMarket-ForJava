package com.example.bookmarket.service;

import com.example.bookmarket.dao.IOrderDao;
import com.example.bookmarket.model.Order;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface IOrderService {
    public List<Order> getOrderList(String oid, String uid, String orderFilter, Integer page, Integer count);
}

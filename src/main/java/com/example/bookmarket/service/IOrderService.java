package com.example.bookmarket.service;

import com.example.bookmarket.model.Order;

import java.util.List;

public interface IOrderService {
    public Integer createOrder(Order order);

    public Integer cancelOrder(Order order);

    public Integer updateOrderStatue(Order order);

    public Order searchOrder(String oid);

    public List<Order> getOrderList(String oid, String uid, String orderFilter, Integer page, Integer count);

    public Long getRecordsByOidAndUidAndStatus(String oid, String uid, String orderFilter);
}

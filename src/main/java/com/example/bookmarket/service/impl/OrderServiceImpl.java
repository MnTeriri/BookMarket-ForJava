package com.example.bookmarket.service.impl;

import com.example.bookmarket.dao.IOrderDao;
import com.example.bookmarket.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao orderDao;

}

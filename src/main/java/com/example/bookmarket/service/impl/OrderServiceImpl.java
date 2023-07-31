package com.example.bookmarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookmarket.dao.IAddressDao;
import com.example.bookmarket.dao.IBookDao;
import com.example.bookmarket.dao.IOrderBookDao;
import com.example.bookmarket.dao.IOrderDao;
import com.example.bookmarket.model.Address;
import com.example.bookmarket.model.Book;
import com.example.bookmarket.model.Order;
import com.example.bookmarket.model.OrderBook;
import com.example.bookmarket.service.IOrderService;
import com.example.bookmarket.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IOrderBookDao orderBookDao;
    @Autowired
    private IBookDao bookDao;
    @Autowired
    private IAddressDao addressDao;

    @Override
    public List<Order> getOrderList(String oid, String uid, String orderFilter, Integer page, Integer count) {
        Page<Order> p = new Page<>(page, count);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().
                like("oid", oid).
                eq("uid", uid).
                orderByDesc("created_time");
        List<Order> list = new ArrayList<>();
        if (orderFilter.equals("notPay")) {//待支付订单
            queryWrapper.eq("status", 0);
        } else if (orderFilter.equals("notReceive")) {//待收货订单
            queryWrapper.and(wrapper -> wrapper.eq("status", 1).or().eq("status", 2));
        } else if (orderFilter.equals("finish")) {//已完成
            queryWrapper.and(wrapper -> wrapper.eq("status", 3).or().eq("status", 6));
        } else if (orderFilter.equals("cancel")) {//已取消
            queryWrapper.and(wrapper -> wrapper.eq("status", 4).or().eq("status", 5));
        }
        list = orderDao.selectPage(p, queryWrapper).getRecords();
        for (Order order : list) {
            Address address = addressDao.selectOne(new QueryWrapper<Address>().eq("id", order.getAid()));
            List<OrderBook> books = orderBookDao.selectList(new QueryWrapper<OrderBook>().eq("oid", order.getOid()));
            for (OrderBook orderBook : books) {
                Book book = bookDao.selectOne(new QueryWrapper<Book>().eq("bid", orderBook.getBid()));
                byte[] image = book.getImage();
                String imageString = "";
                if (image != null) {
                    imageString = ImageUtils.encodeImageString(image);
                }
                book.setImageString(imageString);
                book.setImage(null);
                orderBook.setBook(book);
            }
            order.setAddress(address);
            order.setBooks(books);
        }
        return list;
    }

    @Override
    public Long getRecordsByOidAndUidAndStatus(String oid, String uid, String orderFilter) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().
                like("oid", oid).
                eq("uid", uid);
        if (orderFilter.equals("notPay")) {//待支付订单
            queryWrapper.eq("status", 0);
        } else if (orderFilter.equals("notReceive")) {//待收货订单
            queryWrapper.and(wrapper -> wrapper.eq("status", 1).or().eq("status", 2));
        } else if (orderFilter.equals("finish")) {//已完成
            queryWrapper.and(wrapper -> wrapper.eq("status", 3).or().eq("status", 6));
        } else if (orderFilter.equals("cancel")) {//已取消
            queryWrapper.and(wrapper -> wrapper.eq("status", 4).or().eq("status", 5));
        }
        return orderDao.selectCount(queryWrapper);
    }
}

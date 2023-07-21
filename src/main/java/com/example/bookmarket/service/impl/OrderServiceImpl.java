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
        System.out.println(queryWrapper.getTargetSql());
        if (orderFilter.equals("all")) {
            list = orderDao.selectPage(p, queryWrapper).getRecords();
        }
        for (Order order : list) {
            Address address = addressDao.selectOne(new QueryWrapper<Address>().eq("id", order.getAid()));
            List<OrderBook> books = orderBookDao.selectList(new QueryWrapper<OrderBook>().eq("oid", order.getOid()));
            for (OrderBook orderBook: books) {
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
}

package com.example.bookmarket;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookmarket.dao.*;
import com.example.bookmarket.model.Book;
import com.example.bookmarket.model.User;
import com.example.bookmarket.service.IBookService;
import com.example.bookmarket.service.ICartService;
import com.example.bookmarket.service.IOrderService;
import com.example.bookmarket.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookMarketApplicationTests {
    @Autowired
    private IStorageDao storageDao;
    @Autowired
    private IAddressDao addressDao;
    @Autowired
    private IBookDao bookDao;
    @Autowired
    private IBookService bookService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IOrderService orderService;
    @Test
    void contextLoads() {
        System.out.println(orderService.searchOrder("20230809004041948743"));
    }

}

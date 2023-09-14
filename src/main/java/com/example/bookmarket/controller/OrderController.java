package com.example.bookmarket.controller;

import com.alibaba.fastjson2.JSON;
import com.example.bookmarket.model.Order;
import com.example.bookmarket.service.IOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "http://localhost:8080/", allowCredentials = "true")
public class OrderController {
    @Autowired
    private HttpSession session;
    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "/orderList", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String orderList(String oid, String orderFilter, Integer page) {
        Long recordsFiltered = orderService.getRecordsByOidAndUidAndStatus(oid, "123456789", orderFilter);
        List<Order> orderList = orderService.getOrderList(oid, "123456789", orderFilter, page, 5);
        long totalPage = 0L;//计算总页数，一页显示5条数据
        if (recordsFiltered % 5 == 0) {
            totalPage = recordsFiltered / 5;
        } else {
            totalPage = recordsFiltered / 5 + 1;
        }
        return "{\"totalPage\":" + totalPage + ",\"orderList\":" + JSON.toJSONString(orderList) + "}";
    }

    @RequestMapping(value = "/createOrder", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String createOrder(Order order) {
//        String uid = ((User) session.getAttribute("user")).getUid();
        order.setUid("123456789");
        Integer result = orderService.createOrder(order);
        return "{\"result\":" + result + ",\"order\":" + JSON.toJSONString(order) + "}";
    }

    @RequestMapping(value = "/payOrder", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String payOrder(Integer id) {
        Order order = new Order();
        orderService.cancelOrder(order);
        return "";
    }

    @RequestMapping(value = "/getOrderCount", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String getOrderCount() {
        //String uid = ((User) session.getAttribute("user")).getUid();
        Long notPayOrderCount = orderService.getRecordsByOidAndUidAndStatus("", "123456789", "notPay");
        Long notReceiveOrderCount = orderService.getRecordsByOidAndUidAndStatus("", "123456789", "notReceive");
        return "{\"notPayOrderCount\":" + notPayOrderCount + ",\"notReceiveOrderCount\":" + notReceiveOrderCount + "}";
    }
}

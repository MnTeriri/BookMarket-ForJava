package com.example.bookmarket.controller;

import com.example.bookmarket.service.IOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "http://localhost:8080/", allowCredentials = "true")
public class OrderController {
    @Autowired
    private HttpSession session;
    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "/getNotPayAndNotReceiveCartCount", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String getNotPayAndNotReceiveCartCount() {
        return "";
    }
}

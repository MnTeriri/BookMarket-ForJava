package com.example.bookmarket.controller;

import com.alibaba.fastjson2.JSON;
import com.example.bookmarket.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/address")
@CrossOrigin(origins = "http://localhost:8080/", allowCredentials = "true")
public class AddressController {
    @Autowired
    private IAddressService addressService;
    @RequestMapping(value = "/addressList", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String getAddressList(){
//        String uid = ((User) session.getAttribute("user")).getUid();

        return JSON.toJSONString(addressService.getAddressList("123456789"));
    }
}

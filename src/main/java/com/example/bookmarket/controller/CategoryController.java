package com.example.bookmarket.controller;

import com.alibaba.fastjson2.JSON;
import com.example.bookmarket.dao.ICategoryDao;
import com.example.bookmarket.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "http://localhost:8080/", allowCredentials = "true")
public class CategoryController {
    @Autowired
    private ICategoryDao categoryDao;

    @RequestMapping(value = "/categoryList", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String categoryList() {
        List<Category> list = categoryDao.selectList(null);
        return JSON.toJSONString(list);
    }
}
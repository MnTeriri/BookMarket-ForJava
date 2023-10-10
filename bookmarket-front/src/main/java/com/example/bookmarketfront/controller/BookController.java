package com.example.bookmarketfront.controller;

import com.alibaba.fastjson2.JSON;
import com.example.bookmarketfront.model.Book;
import com.example.bookmarketfront.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@CrossOrigin(origins = "http://localhost:27000/", allowCredentials = "true")
public class BookController {
    @Autowired
    private IBookService bookService;

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @RequestMapping(value = "/bookList", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String bookList(@RequestParam(defaultValue = "") String bname,
                           @RequestParam(defaultValue = "1") Integer cid,
                           @RequestParam(defaultValue = "1") Integer page) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        List<Book> bookList = bookService.getShoppingBookList(bname, cid, page, 12);
        Long recordsFiltered = bookService.getRecordsFilteredByBnameAndCid(bname, cid);
        long totalPage = 0L;//计算总页数，一页显示12条数据
        if (recordsFiltered % 12 == 0) {
            totalPage = recordsFiltered / 12;
        } else {
            totalPage = recordsFiltered / 12 + 1;
        }
        return "{\"bookList\":" + JSON.toJSONString(bookList) +
                ",\"totalPage\":" + totalPage + "}";
    }

    @RequestMapping(value = "/bookDetail", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String bookDetail(String bid) {
        return JSON.toJSONString(bookService.searchBook(bid));
    }
}

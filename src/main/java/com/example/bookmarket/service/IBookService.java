package com.example.bookmarket.service;

import com.example.bookmarket.model.Book;

import java.util.List;

public interface IBookService {
    public Book searchBook(String bid);

    public List<Book> getShoppingBookList(String bname, Integer cid, Integer page, Integer count);

    public Long getRecordsFilteredByBnameAndCid(String bname, Integer cid);
}

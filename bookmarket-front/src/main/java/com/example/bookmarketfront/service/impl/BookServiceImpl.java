package com.example.bookmarketfront.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bookmarketfront.dao.IBookDao;
import com.example.bookmarketfront.dao.ICategoryDao;
import com.example.bookmarketfront.model.Book;
import com.example.bookmarketfront.model.Category;
import com.example.bookmarketfront.service.IBookService;
import com.example.bookmarketfront.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements IBookService {
    @Autowired
    private IBookDao bookDao;
    @Autowired
    private ICategoryDao categoryDao;

    public BookServiceImpl() {
        log.debug("创建服务层实现对象：BookServiceImpl");
    }

    @Override
    public Book searchBook(String bid) {
        return bookDao.searchBookByBid(bid);
    }

    @Override
    public List<Book> getShoppingBookList(String bname, Integer cid, Integer page, Integer count) {
        Page<Book> p = new Page<>(page, count);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("bname", bname).eq("cid", cid);
        List<Book> bookList = bookDao.selectPage(p, queryWrapper).getRecords();
        for (Book book : bookList) {
            byte[] image = book.getImage();
            String imageString = "";
            if (image != null) {
                imageString = ImageUtils.encodeImageString(image);
            }
            book.setImageString(imageString);
            book.setImage(null);
            Category category = categoryDao.selectOne(new QueryWrapper<Category>().eq("id", book.getCid()));
            book.setCname(category.getCname());
        }
        return bookList;
    }

    @Override
    public Long getRecordsFilteredByBnameAndCid(String bname, Integer cid) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("bname", bname).eq("cid", cid).eq("status", 0);
        return bookDao.selectCount(queryWrapper);
    }
}

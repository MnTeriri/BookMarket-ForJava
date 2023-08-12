package com.example.bookmarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarket.model.Book;
import com.example.bookmarket.util.ImageUtils;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookDao extends BaseMapper<Book> {
    @Select("SELECT * FROM Book WHERE bid=#{bid}")
    @Results({
            @Result(property = "imageString", column = "image", typeHandler = ImageUtils.class)
    })
    public Book searchBookByBid(String bid);
}

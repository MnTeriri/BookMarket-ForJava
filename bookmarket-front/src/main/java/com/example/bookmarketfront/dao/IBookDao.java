package com.example.bookmarketfront.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarketfront.model.Book;
import com.example.bookmarketfront.util.ImageUtils;
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

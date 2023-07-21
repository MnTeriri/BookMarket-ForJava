package com.example.bookmarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarket.model.Cart;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface ICartDao extends BaseMapper<Cart> {
    /**
     * 查询购物车选中数量和总数时，需要过滤掉
     * 1、书籍状态不为正常状态
     * 2、书籍数量比购物车数量少的
     * 这些购物车信息是异常信息
     */
    @Select("SELECT COUNT(Cart.id) FROM Cart INNER JOIN Book ON Cart.bid = Book.bid " +
            "WHERE uid=#{uid} AND selected=1 AND Book.status=0 AND Book.count>=Cart.count")
    public Long getSelectedCartCount(String uid);

    @Select("SELECT COUNT(Cart.id) FROM Cart INNER JOIN Book ON Cart.bid = Book.bid " +
            "WHERE uid=#{uid} AND Book.status=0 AND Book.count>=Cart.count")
    public Long getTotalCartCount(String uid);
}

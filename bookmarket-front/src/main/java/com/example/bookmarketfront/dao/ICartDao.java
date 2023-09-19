package com.example.bookmarketfront.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarketfront.model.Cart;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartDao extends BaseMapper<Cart> {
    @Select("SELECT * FROM Cart WHERE uid=#{uid}")
    @Results({
            @Result(property = "bid", column = "bid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "book", column = "bid", one = @One(select = "com.example.bookmarketfront.dao.IBookDao.searchBookByBid"))
    })
    public List<Cart> getCartList(@Param("uid") String uid);

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

    /**
     * 对购物车全选或者取消全选时，需要过滤掉
     * 1、书籍状态不为正常状态
     * 2、书籍数量比购物车数量少的
     * 这些购物车信息是异常信息
     */
    @Update("UPDATE Cart INNER JOIN Book ON Cart.bid = Book.bid " +
            "SET Cart.selected=#{cart.selected},Cart.add_time=now() " +
            "WHERE Cart.uid=#{cart.uid} AND Book.status=0 AND Book.count>=Cart.count")
    public boolean updateAllCartSelected(@Param("cart") Cart cart);
}

package com.example.bookmarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.bookmarket.dao.IBookDao;
import com.example.bookmarket.dao.ICartDao;
import com.example.bookmarket.model.Book;
import com.example.bookmarket.model.Cart;
import com.example.bookmarket.service.ICartService;
import com.example.bookmarket.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private ICartDao cartDao;
    @Autowired
    private IBookDao bookDao;

    public CartServiceImpl() {
        log.debug("创建服务层实现对象：CartServiceImpl");
    }

    @Override
    public String addCart(Cart cart) {
        Book book = bookDao.selectOne(new QueryWrapper<Book>().eq("bid", cart.getBid()));
        if (book.getStatus() != 0) {//书籍状态为缺货或者下架，无法添加
            return "-1";
        }
        Cart data = cartDao.selectOne(new QueryWrapper<Cart>().
                eq("uid", cart.getUid()).
                eq("bid", cart.getBid()));
        if (data == null) {//如果购物车中不存在该商品
            if (cartDao.insert(cart) == 1) {
                return "1";
            }
        } else {//如果购物车中存在该商品
            if (book.getCount() == data.getCount()) {//书籍数量不足以新增
                return "-2";
            }
            data.setCount(data.getCount() + 1);//数量加1
            if (cartDao.updateById(data) == 1) {
                return "1";
            }
        }
        return "-3";
    }

    @Override
    public List<Cart> getCartList(String uid) {
        List<Cart> cartList = cartDao.getCartList(uid);
        for (Cart cart : cartList) {
            Book book = cart.getBook();
            UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>().
                    set("selected", 0).eq("id", cart.getId());
            if (cart.getCount() > book.getCount() || book.getStatus() != 0) {
                //如果查询出的购物车商品数量比书籍数量还多，需要取消该购物车选择，此条购物车信息是错误信息
                //如果查询出的购物车商品状态为下架或者缺货，需要取消该购物车选择，此条购物车信息是错误信息
                cartDao.update(null, updateWrapper);
            }
            cart.setBook(book);
        }
        return cartList;
    }

    @Override
    public Long getSelectedCartCount(String uid) {
        return cartDao.getSelectedCartCount(uid);
    }

    @Override
    public Long getTotalCartCount(String uid) {
        return cartDao.getTotalCartCount(uid);
    }

    @Override
    public String updateCartSelect(Cart cart) {
        cart=cartDao.selectById(cart.getId());
        if (cart == null) {
            return "-3";//未知错误
        }
        Book book = bookDao.selectOne(new QueryWrapper<Book>().eq("bid",cart.getBid()));
        if (book.getStatus() != 0) {//书籍状态不正常，不允许修改购物车选中状态
            return "-2";
        }
        if (cart.getSelected() == 0) {
            cart.setSelected(1);
        } else {
            cart.setSelected(0);
        }
        if (cartDao.updateById(cart)==1) {
            return "1";
        }
        return "-1";
    }

    @Override
    public String updateAllCartSelect(Cart cart) {
        if(cartDao.updateAllCartSelected(cart)){
            return "1";
        }
        return "-1";
    }

    @Override
    public String addCartCount(Cart cart) {
        Cart data = cartDao.selectById(cart.getId());
        if (data == null) {
            return "-3";//未知错误
        }
        Book book = bookDao.selectOne(new QueryWrapper<Book>().eq("bid", data.getBid()));
        if (data.getCount() >= book.getCount()) {//购物车数量大于等于库存数量，不允许新增
            return "-2";
        }
        data.setCount(data.getCount() + 1);
        if (cartDao.update(data, new UpdateWrapper<Cart>().eq("id", data.getId())) == 1) {
            return "1";
        }
        return "-1";
    }

    @Override
    public String subCartCount(Cart cart) {
        Cart data = cartDao.selectById(cart.getId());
        if (data == null) {
            return "-3";//未知错误
        }
        if (data.getCount() <= 1) {//购物车数量少于1，不允许减少
            return "-2";
        }
        data.setCount(data.getCount() - 1);
        if (cartDao.update(data, new UpdateWrapper<Cart>().eq("id", data.getId())) == 1) {
            return "1";
        }
        return "-1";
    }

    @Override
    public String deleteCart(Cart cart) {
        if (cartDao.deleteById(cart) == 1) {
            return "1";
        }
        return "-1";
    }
}

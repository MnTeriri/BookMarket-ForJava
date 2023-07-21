package com.example.bookmarket.service;

import com.example.bookmarket.model.Cart;

import java.util.List;

public interface ICartService {
    public String addCart(Cart cart);

    public List<Cart> getCartList(String uid);

    public Long getSelectedCartCount(String uid);

    public Long getTotalCartCount(String uid);

    public String addCartCount(Cart cart);

    public String subCartCount(Cart cart);

    public String deleteCart(Cart cart);
}

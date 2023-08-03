package com.example.bookmarket.controller;

import com.alibaba.fastjson2.JSON;
import com.example.bookmarket.model.Address;
import com.example.bookmarket.model.Cart;
import com.example.bookmarket.model.User;
import com.example.bookmarket.service.IAddressService;
import com.example.bookmarket.service.ICartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:8080/", allowCredentials = "true")
public class CartController {
    @Autowired
    private HttpSession session;
    @Autowired
    private ICartService cartService;
    @Autowired
    private IAddressService addressService;

    @RequestMapping(value = "/cartList", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String getCartList() {
//        String uid = ((User) session.getAttribute("user")).getUid();
        List<Cart> cartList = cartService.getCartList("123456789");
        Long selectedCartCount = cartService.getSelectedCartCount("123456789");
        Long totalCartCount = cartService.getTotalCartCount("123456789");
        Address address = addressService.searchDefaultAddress("123456789");
        return "{\"cartList\":" + JSON.toJSONString(cartList) +
                ",\"selectedCartCount\":" + selectedCartCount +
                ",\"totalCartCount\":" + totalCartCount +
                ",\"address\":" + JSON.toJSONString(address) + "}";
    }

    @RequestMapping(value = "/addCart", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String addCart(Cart cart) {
        cart.setUid("123456789");
        return cartService.addCart(cart);
    }

    @RequestMapping(value = "/selectCart", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String selectCart(Cart cart) {
        return cartService.updateCartSelect(cart);
    }

    @RequestMapping(value = "/selectAllCart", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String selectAllCart(Cart cart) {
//        String uid = ((User) session.getAttribute("user")).getUid();
        cart.setUid("123456789");
        return cartService.updateAllCartSelect(cart);
    }
    @RequestMapping(value = "/addCartCount", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String addCartCount(Cart cart) {
        return cartService.addCartCount(cart);
    }

    @RequestMapping(value = "/subCartCount", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String subCartCount(Cart cart) {
        return cartService.subCartCount(cart);
    }

    @RequestMapping(value = "/deleteCart", produces = "application/json;charset=utf-8", method = {RequestMethod.POST, RequestMethod.GET})
    public String deleteCart(Cart cart) {
        return cartService.deleteCart(cart);
    }
}

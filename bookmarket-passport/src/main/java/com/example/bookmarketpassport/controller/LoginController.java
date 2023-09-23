package com.example.bookmarketpassport.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.example.bookmarketpassport.model.LoginUser;
import com.example.bookmarketpassport.model.User;
import com.example.bookmarketpassport.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/LoginController")
public class LoginController {
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(String uid, String password) {
        User user = new User();
        user.setUid(uid);
        user.setPassword(password);
        userService.login(user);
        return "1111111111";
    }
}

package com.example.bookmarketpassport.controller;

import com.example.bookmarketpassport.model.User;
import com.example.bookmarketpassport.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

package com.example.bookmarketpassport.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.example.bookmarketpassport.model.LoginUser;
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
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(String uid,String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uid, DigestUtil.md5Hex(password));
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        System.out.println("loginUser:"+loginUser);
        return "11111111";
    }
}

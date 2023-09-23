package com.example.bookmarketpassport.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.example.bookmarketpassport.model.LoginUser;
import com.example.bookmarketpassport.model.User;
import com.example.bookmarketpassport.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUid(), DigestUtil.md5Hex(user.getPassword()));
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        System.out.println("loginUser:" + loginUser);
        return null;
    }

    @Override
    public String register(User user) {
        return null;
    }
}

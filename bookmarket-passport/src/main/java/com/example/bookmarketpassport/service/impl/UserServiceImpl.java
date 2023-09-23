package com.example.bookmarketpassport.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.example.bookmarketpassport.model.LoginUser;
import com.example.bookmarketpassport.model.User;
import com.example.bookmarketpassport.service.IUserService;
import com.example.bookmarketpassport.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUid(), DigestUtil.md5Hex(user.getPassword()));
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        RedisUtils.setCacheObject(user.getUid(), loginUser);
        log.debug("登录信息存放到Redis：{}", loginUser);
        return null;
    }

    @Override
    public String register(User user) {
        return null;
    }
}

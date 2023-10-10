package com.example.bookmarketpassport.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bookmarketpassport.dao.IUserDao;
import com.example.bookmarketpassport.model.LoginUser;
import com.example.bookmarketpassport.model.User;
import com.example.bookmarketpassport.service.IUserService;
import com.example.bookmarketpassport.utils.ImageUtils;
import com.example.bookmarketpassport.utils.JwtUtils;
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
    @Autowired
    private IUserDao userDao;

    @Override
    public String login(User user) {
        //判定用户和密码
        User selected = userDao.selectOne(new QueryWrapper<User>().eq("uid", user.getUid()));
        if (selected == null) {
            return "{\"code\": -2}";//没有用户
        }
        String md5Password = DigestUtil.md5Hex(user.getPassword());
        if (!selected.getPassword().equals(md5Password)) {
            return "{\"code\": -3}";//密码错误
        }
        selected.setImageString(ImageUtils.encodeImageString(selected.getImage()));
        selected.setImage(null);

        //查询权限信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUid(), DigestUtil.md5Hex(user.getPassword()));
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        RedisUtils.setCacheObject(user.getUid(), loginUser);
        //生成token
        String token = JwtUtils.createJWT(user.getUid());
        log.debug("登录信息存放到Redis：{}", loginUser);
        log.debug("token信息：{}", token);
        String respCode = "{\"code\": 1," +
                "\"token\": \"" + token + "\"," +
                "\"userJSON\":" + JSON.toJSONString(selected) + "}";

        return "{\"respCode\": " + respCode + ",\"userFlag\": " + selected.getFlag() + "}";
    }

    @Override
    public String register(User user) {
        return null;
    }
}

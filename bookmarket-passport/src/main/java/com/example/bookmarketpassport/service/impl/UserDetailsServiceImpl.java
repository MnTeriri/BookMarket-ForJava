package com.example.bookmarketpassport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bookmarketpassport.dao.IUserDao;
import com.example.bookmarketpassport.model.LoginUser;
import com.example.bookmarketpassport.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IUserDao userDao;

    public UserDetailsServiceImpl() {
        log.debug("创建自定义UserDetailsService对象：UserDetailsServiceImpl");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", username);
        log.debug("用户{}信息查询成功", username);
        User user = userDao.selectOne(queryWrapper);
        user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(user.getPassword()));
        ArrayList<String> strings = new ArrayList<>();
        strings.add("admin:user");
        return new LoginUser(user, strings);
    }
}

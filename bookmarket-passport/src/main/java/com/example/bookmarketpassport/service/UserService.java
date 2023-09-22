package com.example.bookmarketpassport.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bookmarketpassport.dao.IUserDao;
import com.example.bookmarketpassport.model.LoginUser;
import com.example.bookmarketpassport.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private IUserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", username);
        User user = userDao.selectOne(queryWrapper);
        user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(user.getPassword()));
        System.out.println("userMsg:" + user);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("admin:user");
        return new LoginUser(user, strings);
    }
}

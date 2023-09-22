package com.example.bookmarketpassport.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarketpassport.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends BaseMapper<User> {

}

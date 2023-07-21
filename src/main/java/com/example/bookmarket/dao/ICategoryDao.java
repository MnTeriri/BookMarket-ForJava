package com.example.bookmarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarket.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryDao extends BaseMapper<Category> {
}

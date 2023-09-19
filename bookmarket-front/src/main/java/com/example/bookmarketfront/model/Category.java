package com.example.bookmarketfront.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Category")
public class Category {
    private Integer id;//分类号
    private String cname;//分类名
}

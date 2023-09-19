package com.example.bookmarketfront.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Cart")
public class Cart {
    private Integer id;
    private String uid;//用户编号
    private String bid;//图书编号
    @TableField(exist = false)
    private Book book;//图书信息
    private Integer count;//选购数量
    private LocalDateTime addTime;//加购时间
    private Integer selected;//0为未选购，1为选购
}

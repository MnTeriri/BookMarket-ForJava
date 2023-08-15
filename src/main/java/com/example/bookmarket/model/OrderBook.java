package com.example.bookmarket.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Order_Book")
public class OrderBook implements Serializable {
    private Integer id;
    private String oid;//订单编号
    private String bid;//图书编号
    @TableField(exist = false)
    private Book book;//图书信息
    private Integer count;//选购数量
    private BigDecimal price;//价格
    private BigDecimal discount;//折扣
}

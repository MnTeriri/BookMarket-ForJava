package com.example.bookmarket.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`Order`")
public class Order {
    private Integer id;
    private String oid;//订单编号
    private String uid;//用户编号
    private Integer aid;//地址编号
    @TableField(exist = false)
    private Address address;//地址信息
    @TableField(exist = false)
    private List<OrderBook> books;//订单当中购买书籍的信息
    private BigDecimal price;//总金额
    private Integer status;//订单状态 0待付款、1已付款、2已发货、3交易成功、4交易取消、5退货、6退货成功
    private LocalDateTime createdTime;//创建时间
    private LocalDateTime payTime;//付款时间
    private LocalDateTime sendTime;//发货时间
    private LocalDateTime finishTime;//完成时间
}

package com.example.bookmarketadmin.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Storage")
public class Storage {
    private Integer id;
    private String bid;//图书编号
    @TableField(exist = false)
    private Book book;//图书信息
    private String uid;//用户编号
    @TableField(exist = false)
    private User user;//用户信息
    private Integer amount;//数量
    private Integer status;//状态 0入库、1卖出、2退货、3取消订单
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime time;//操作时间
}

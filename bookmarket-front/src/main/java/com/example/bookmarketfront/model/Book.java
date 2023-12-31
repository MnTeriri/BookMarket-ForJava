package com.example.bookmarketfront.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.bookmarketfront.util.ImageUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Book")
public class Book implements Serializable {
    private Integer id;
    private String bid;//图书编号（ISBN）
    private Integer cid;//分类号
    @TableField(exist = false)
    private String cname;//分类名
    private String bname;//图书名称
    private String author;//作者名称
    private String publisher;//出版社名称
    @JSONField(format = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime publishTime;//出版时间
    private byte[] image;
    @TableField(exist = false)
    private String imageString;//书籍图片
    private BigDecimal price;//价格
    private BigDecimal discount;//折扣
    private Integer count;//数量
    private String description;//图书描述
    private Integer status;//图书状态 0正常、1缺货、2下架
}

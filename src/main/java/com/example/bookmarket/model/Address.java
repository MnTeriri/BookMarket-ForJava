package com.example.bookmarket.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("Address")
public class Address implements Serializable {
    private Integer id;//地址编号
    private String uid;//用户编号
    private String province;//省
    private String city;//市
    private String district;//区
    private String address;//详细地址
    private String receiverName;//收货人
    private String phone;//手机号码
    private Integer isDefault;//0不选中、1为选中
    private Integer status;//0为假删除，1为未删除
}

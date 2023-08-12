package com.example.bookmarket.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bookmarket.model.Address;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressDao extends BaseMapper<Address> {
    @Select("SELECT * FROM Address WHERE id=#{aid}")
    public Address searchById(Integer aid);

    @Update("UPDATE Address SET is_default=0 WHERE uid=#{uid}")
    public boolean updateAddressDefault(String uid);

    @Select("SELECT * FROM Address WHERE uid=#{uid} AND is_default=1")
    public Address searchDefaultAddress(String uid);
}

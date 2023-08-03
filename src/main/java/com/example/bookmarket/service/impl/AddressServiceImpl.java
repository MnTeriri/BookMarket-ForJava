package com.example.bookmarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bookmarket.dao.IAddressDao;
import com.example.bookmarket.model.Address;
import com.example.bookmarket.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressDao addressDao;

    @Override
    public ArrayList<Address> findAddress(String uid) {
        return null;
    }

    @Override
    public String addAddress(Address address) {
        return null;
    }

    @Override
    public String deleteAddress(Address address) {
        return null;
    }

    @Override
    public String updateAddress(Address address) {
        return null;
    }

    @Override
    public Address searchDefaultAddress(String uid) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<Address>()
                .eq("uid", uid)
                .eq("is_default", 1);
        return addressDao.selectOne(queryWrapper);
    }
}
package com.example.bookmarketfront.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bookmarketfront.dao.IAddressDao;
import com.example.bookmarketfront.model.Address;
import com.example.bookmarketfront.service.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressDao addressDao;

    public AddressServiceImpl() {
        log.debug("创建服务层实现对象：AddressServiceImpl");
    }

    @Override
    public List<Address> getAddressList(String uid) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<Address>().eq("uid", uid);
        return addressDao.selectList(queryWrapper);
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
        return addressDao.searchDefaultAddress(uid);
    }
}

package com.example.bookmarketfront.service;

import com.example.bookmarketfront.model.Address;

import java.util.List;

public interface IAddressService {
    public List<Address> getAddressList(String uid);

    public String addAddress(Address address);

    public String deleteAddress(Address address);

    public String updateAddress(Address address);

    public Address searchDefaultAddress(String uid);
}

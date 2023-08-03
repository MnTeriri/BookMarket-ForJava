package com.example.bookmarket.service;

import com.example.bookmarket.model.Address;

import java.util.ArrayList;

public interface IAddressService {
    public ArrayList<Address> findAddress(String uid);

    public String addAddress(Address address);

    public String deleteAddress(Address address);

    public String updateAddress(Address address);

    public Address searchDefaultAddress(String uid);
}

package com.example.bookmarket.service;

import com.example.bookmarket.model.User;

public interface IUserService {
    //public UserTableObject showUserTable(Integer draw, String searchValue, Integer start, Integer length, Integer column, String dir);

    public User searchUser(String uid);

    public String updateUser(User user);

    public String updateUserPassword(String uid, String oldPassword, String newPassword);

    public String login(User user);

    public String register(User user);
}

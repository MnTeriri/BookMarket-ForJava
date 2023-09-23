package com.example.bookmarketpassport.service;

import com.example.bookmarketpassport.model.User;

public interface IUserService {
    public String login(User user);

    public String register(User user);
}

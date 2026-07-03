package com.greet.service;

import com.greet.model.User;

public interface UserService {

    User login(String username, String password);

    boolean register(User user);
}
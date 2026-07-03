package com.greet.service;

import com.greet.model.User;
import java.util.Optional;

public interface UserService {
    boolean register(User user);
    Optional<User> login(String username, String password);
}
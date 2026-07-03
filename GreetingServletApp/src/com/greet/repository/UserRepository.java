package com.greet.repository;

import com.greet.model.User;

public interface UserRepository {

    User findByUsername(String username);

    boolean save(User user);
}
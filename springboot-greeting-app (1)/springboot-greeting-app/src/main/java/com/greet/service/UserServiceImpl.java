package com.greet.service;
import com.greet.model.User;
import com.greet.repository.UserRepository;
import com.greet.util.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public boolean register(User user) {
        log.debug("Attempting to register user: {}", user.getUsername());
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            log.warn("Registration failed. Username '{}' is already taken",
                    user.getUsername());
            return false;
        }
        user.setPassword(HashUtil.hashPassword(user.getPassword()));
        userRepository.save(user);
        log.info("User '{}' registered successfully with ID {}",
                user.getUsername(), user.getId());
        return true;
    }
    @Override
    public Optional<User> login(String username, String password) {
        log.debug("Attempting authentication login for user: {}", username);
        return userRepository.findByUsername(username)
                .filter(u ->
                        u.getPassword().equals(HashUtil.hashPassword(password)))
                .map(u -> {
                    log.info("Authentication successful for user: {}", username);
                    return u;
                });
    }
}
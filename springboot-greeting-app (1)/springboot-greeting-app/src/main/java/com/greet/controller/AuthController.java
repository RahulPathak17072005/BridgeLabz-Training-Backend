package com.greet.controller;
import com.greet.dto.UserRegistrationDto;
import com.greet.dto.UserLoginDto;
import com.greet.dto.UserResponseDto;
import com.greet.model.User;
import com.greet.service.UserService;
import com.greet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto
                                              registrationDto) {
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(registrationDto.getPassword());
        user.setEmail(registrationDto.getEmail());
        user.setRole(registrationDto.getRole());
        if (userService.register(user)) {
            UserResponseDto responseDto = UserResponseDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "user", responseDto
            ));
        }
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Username is already taken"));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {
        return userService.login(loginDto.getUsername(), loginDto.getPassword())
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername(),
                            user.getRole().name());
                    return ResponseEntity.ok(Map.of(
                            "message", "Login successful",
                            "token", "Bearer " + token,
                            "role", user.getRole()
                    ));
                })
                .orElse(ResponseEntity.status(401)
                        .body(Map.of("error", "Invalid username or password")));
    }
}

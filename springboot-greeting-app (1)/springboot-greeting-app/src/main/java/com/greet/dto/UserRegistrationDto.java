package com.greet.dto;
import com.greet.model.Role;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    private String username;
    private String password;
    private String email;
    private Role role;
}
package com.greet.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    private String username;
    private String password;
}
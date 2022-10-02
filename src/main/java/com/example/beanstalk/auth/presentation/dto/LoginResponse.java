package com.example.beanstalk.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private AccessTokenUserData user;
    private String token;
}

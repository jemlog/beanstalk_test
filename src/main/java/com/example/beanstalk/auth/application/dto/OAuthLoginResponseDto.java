package com.example.beanstalk.auth.application.dto;


import com.example.beanstalk.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OAuthLoginResponseDto {

    private String accessToken;
    private Boolean isNewUser;
    private User user;


}

package com.example.beanstalk.auth.client.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GoogleUserResponse {

    private String sub;
    private String email;
    private Boolean email_verified;

}

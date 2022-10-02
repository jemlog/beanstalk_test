package com.example.beanstalk.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckDuplicateNicknameResponse {

    private int code;
    private String message;

}

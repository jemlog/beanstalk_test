package com.example.beanstalk.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckDuplicateEmailResponse {

    private int code;
    private String message;

}

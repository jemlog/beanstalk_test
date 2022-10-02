package com.example.beanstalk.auth.exception.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

    int errorCode;
    String message;
}

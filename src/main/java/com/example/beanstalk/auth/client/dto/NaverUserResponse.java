package com.example.beanstalk.auth.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverUserResponse {
    private String resultcode;
    private String message;
    private Response response;

    @Data
    @NoArgsConstructor
    public class Response{

        private String id;
        private String email;
        private String nickname;



    }
}

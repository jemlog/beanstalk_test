package com.example.beanstalk.auth.client;


import com.example.beanstalk.user.domain.User;

public interface Client {

    public User getUserData(String accessToken);
}

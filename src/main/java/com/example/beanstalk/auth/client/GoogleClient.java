package com.example.beanstalk.auth.client;


import com.example.beanstalk.auth.client.dto.GoogleUserResponse;
import com.example.beanstalk.auth.exception.TokenValidFailedException;
import com.example.beanstalk.auth.presentation.dto.Provider;
import com.example.beanstalk.common.exception.ExceptionCodeAndDetails;
import com.example.beanstalk.common.exception.GlobalBadRequestException;
import com.example.beanstalk.user.domain.User;
import com.example.beanstalk.user.domain.enums.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleClient implements Client{

    private final WebClient webClient;

    @Override
    public User getUserData(String accessToken) {

        GoogleUserResponse googleUserResponse = webClient.get()
                .uri("https://oauth2.googleapis.com/tokeninfo", builder -> builder.queryParam("id_token", accessToken).build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new GlobalBadRequestException(ExceptionCodeAndDetails.GOOGLE_ACCESS)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new TokenValidFailedException("Internal Server Error")))
                .bodyToMono(GoogleUserResponse.class)
                .block();

        return User.builder()
                .uid(googleUserResponse.getSub())
                .provider(Provider.GOOGLE)
                .password("NO_PASSWORD")
                .roleType(RoleType.USER).build();



    }
}

package com.example.beanstalk.auth.client;


import com.example.beanstalk.auth.client.dto.NaverUserResponse;
import com.example.beanstalk.auth.exception.TokenValidFailedException;
import com.example.beanstalk.auth.presentation.dto.Provider;
import com.example.beanstalk.common.exception.ExceptionCodeAndDetails;
import com.example.beanstalk.common.exception.GlobalBadRequestException;
import com.example.beanstalk.user.domain.User;
import com.example.beanstalk.user.domain.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NaverClient implements Client{

    private final WebClient webClient;

    @Override
    public User getUserData(String accessToken) {

        NaverUserResponse naverUserResponse = webClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new GlobalBadRequestException(ExceptionCodeAndDetails.NAVER_ACCESS)))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new TokenValidFailedException("Internal Server error")))
                .bodyToMono(NaverUserResponse.class)
                .block();

        return User.builder()
                .uid(naverUserResponse.getResponse().getId())
                .provider(Provider.NAVER)
                .roleType(RoleType.USER)
                .password("NO_PASSWORD")
                .build();
    }
}

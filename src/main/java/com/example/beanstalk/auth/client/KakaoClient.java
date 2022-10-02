package com.example.beanstalk.auth.client;


import com.example.beanstalk.auth.client.dto.KakaoUserResponse;
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
public class KakaoClient implements Client{

    private final WebClient webClient;

    @Override
    public User getUserData(String accessToken) {

        KakaoUserResponse kakaoUserResponse = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new GlobalBadRequestException(ExceptionCodeAndDetails.KAKAO_ACCESS)))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new TokenValidFailedException("Internal Server error")))
                .bodyToMono(KakaoUserResponse.class)
                .block();

        return User.builder()
                .uid(String.valueOf(kakaoUserResponse.getId()))
                .provider(Provider.KAKAO)
                .roleType(RoleType.USER)
                .password("NO_PASSWORD")
                .build();
    }
}

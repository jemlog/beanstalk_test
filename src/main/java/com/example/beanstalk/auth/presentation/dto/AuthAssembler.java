package com.example.beanstalk.auth.presentation.dto;


import com.example.beanstalk.auth.application.dto.AuthRequestForSignupDto;
import com.example.beanstalk.common.response.CommonResponse;
import com.example.beanstalk.user.domain.User;
import com.example.beanstalk.user.domain.enums.RoleType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class AuthAssembler {

   public static User createEmailLoginUser(AuthRequestForSignupDto authRequestForSignupDto, PasswordEncoder passwordEncoder, String filename)
   {
       //passwordEncoder.encode(authRequestForSignupDto.getPassword())
       return User.builder()
               .email(authRequestForSignupDto.getEmail())
               .password(authRequestForSignupDto.getPassword())
               .roleType(RoleType.USER)
               .profileImage(filename)
               .ageGroup(authRequestForSignupDto.getAgeGroup())
               .gender(authRequestForSignupDto.getGender())
               .uid(UUID.randomUUID().toString())
               .provider(Provider.DEFAULT)
               .nickname(authRequestForSignupDto.getNickname())
               .build();
   }


   public static AuthRequestForSignupDto authRequestForSignupDto(AuthRequestForSignup authRequestForSignup)
    {
        return new AuthRequestForSignupDto(authRequestForSignup.getEmail(),
                authRequestForSignup.getPassword(),
                authRequestForSignup.getNickname(),
                authRequestForSignup.getAgeGroup(),
                authRequestForSignup.getGender(),
                authRequestForSignup.getProfileImage());
    }

    public static CommonResponse authUserDataResponse(User user)
    {
        return new CommonResponse(200,"인증 유저 정보",new AuthmeWrappingDto(new AccessTokenUserData(user.getUserSeq(),

                user.getUserId(),
                user.getProvider(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                user.getGender(),
                user.getAgeGroup())
                ));
    }

    public static  LoginResponse loginResponse(String accessToken,User user)
    {
        return new LoginResponse(new AccessTokenUserData(user.getUserSeq(),
                user.getUserId(),
                user.getProvider(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                user.getGender(),
                user.getAgeGroup()),
                accessToken);
    }

    public static  SignupResponse signupResponse(String accessToken,User user)
    {
        return new SignupResponse(new AccessTokenUserData(user.getUserSeq(),
                user.getUserId(),
                user.getProvider(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                user.getGender(),
                user.getAgeGroup()),
                accessToken);
    }


    public static CommonResponse oAuthLoginResponse(String token, boolean isNewUser, User user)
    {
        if(isNewUser == true)
        {
            return new CommonResponse(200,
                    "회원가입이 필요합니다",
                    new OAuthLoginResponse(
                            isNewUser,AccessTokenUserData.builder().uid(user.getUserId()).build())
            );
        }
        else{
            return new CommonResponse(200,
                    "로그인 완료",
                    new OAuthLoginResponse(new AccessTokenUserData(user.getUserSeq(),
                            user.getUserId(),
                            user.getProvider(),
                            user.getEmail(),
                            user.getNickname(),
                            user.getProfileImage(),
                            user.getGender(),
                            user.getAgeGroup()),
                            token,
                            isNewUser)
                    );
        }
    }
}

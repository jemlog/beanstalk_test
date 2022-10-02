package com.example.beanstalk.auth.application;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.beanstalk.auth.application.dto.OAuthLoginResponseDto;
import com.example.beanstalk.auth.client.AppleClient;
import com.example.beanstalk.auth.client.GoogleClient;
import com.example.beanstalk.auth.client.KakaoClient;
import com.example.beanstalk.auth.client.NaverClient;
import com.example.beanstalk.auth.presentation.dto.AuthAssembler;
import com.example.beanstalk.auth.presentation.dto.AuthRequest;
import com.example.beanstalk.auth.presentation.dto.AuthRequestForOAuthSignup;
import com.example.beanstalk.auth.presentation.dto.LoginResponse;
import com.example.beanstalk.auth.token.AuthToken;
import com.example.beanstalk.auth.token.JwtTokenProvider;
import com.example.beanstalk.common.exception.ExceptionCodeAndDetails;
import com.example.beanstalk.common.exception.GlobalBadRequestException;
import com.example.beanstalk.common.exception.GlobalServerException;
import com.example.beanstalk.common.util.aws.AWSS3UploadService;
import com.example.beanstalk.user.domain.User;
import com.example.beanstalk.user.domain.UserRepository;
import com.example.beanstalk.user.domain.enums.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;
    private final GoogleClient googleClient;
    private final AppleClient appleClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final AWSS3UploadService uploadService;

    @Transactional
    public OAuthLoginResponseDto login(AuthRequest authRequest) {

        User socialUser;
        switch (authRequest.getProvider()){

            case KAKAO:
                    socialUser = kakaoClient.getUserData(authRequest.getAccessToken());
                    break;
            case NAVER:
                    socialUser = naverClient.getUserData(authRequest.getAccessToken());
                    break;
            case GOOGLE:
                    socialUser = googleClient.getUserData(authRequest.getAccessToken());
                    break;

            case APPLE:
                socialUser = appleClient.getUserData(authRequest.getAccessToken());
                break;

            default:
                socialUser = null;
                break;

        }

        if(socialUser == null)
        {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.APPLE_ACCESS);
        }

        Optional<User> user = userRepository.findUserByUserId(socialUser.getUserId());
        // 이미 회원가입한 유저라면?
        if(user.isPresent())
        {
            // 사용자가 가입을 진행하다가 중간에 설정이 귀찮아서 나갔다가 다시 들어올 수도 있음
            // 닉네임은 필수 정보 이기때문에 현재 DB에 저장된 소셜 로그인 유저가 닉네임을 가지고 있는지 여부로 회원가입 여부 판단
            if(user.get().getNickname() != null)
            {
                String accessToken = getToken(socialUser.getUserId());
                return new OAuthLoginResponseDto(accessToken,false,user.get());
            }
            else{
                return OAuthLoginResponseDto.builder().isNewUser(true).user(User.builder().uid(socialUser.getUserId()).build()).build();
            }
        }
       // 새로운 유저라면 아직 추가정보 기입 안한 상태 -> isNewUser가 true라면, 딴거 다 필요없고 isNewUser만 있으면 됨
       // signup 로직에서 추가정보 다 기입 받고 나면 실제 유저 정보와 토큰 발급 해주는게 좋을듯
        // signup 도중에 밖에 나간다면, 회원가입 안된 상태 -> 똑같이 isNewUser true만 발급

        userRepository.save(socialUser);

        return OAuthLoginResponseDto.builder().isNewUser(true).user(User.builder().uid(socialUser.getUserId()).build()).build();

    }

    private String getToken(String userId)
    {
        AuthToken accessToken = jwtTokenProvider.createToken(userId, RoleType.USER, "20000");
        return accessToken.getToken();
    }

    @Transactional
    public LoginResponse signup(AuthRequestForOAuthSignup authRequestForOAuthSignup) {

        log.info("보자 = {}",authRequestForOAuthSignup.getUid());
        // 일단 유저 찾고
        User user = userRepository.findUserByUserId(authRequestForOAuthSignup.getUid()).orElseThrow(()->{throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);});
        // 있으면 업데이트 하고
        user.updateUser(authRequestForOAuthSignup.getNickname(),authRequestForOAuthSignup.getGender(),getMultipartFileName(authRequestForOAuthSignup.getProfileImage()),authRequestForOAuthSignup.getAgeGroup());
        // 회원가입 완료 됐으니깐 토큰 생성
        String token = getToken(user.getUserId());
        // 반환 해주기
        return AuthAssembler.loginResponse(token, user);

    }


    private String getMultipartFileName(MultipartFile file) {

        if(file != null)
        {
            String fileNameList;
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                uploadService.uploadFile(inputStream,objectMetadata,fileName);
            } catch(IOException e) {
                throw new GlobalServerException();
            }
            fileNameList = uploadService.getFileUrl(fileName);

            return fileNameList;
        }
        return null;
    }

    // TODO : 차후에 로그인 회원 정보로 prefix 만들면 될 것 같음!
    private String createFileName(String fileName) {
        return "user1/" + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식 입니다.");
        }
    }
}
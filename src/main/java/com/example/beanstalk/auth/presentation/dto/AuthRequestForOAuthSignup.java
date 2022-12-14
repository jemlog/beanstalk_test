package com.example.beanstalk.auth.presentation.dto;


import com.example.beanstalk.user.domain.enums.AgeGroup;
import com.example.beanstalk.user.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequestForOAuthSignup {

    @Schema(example = "소셜 아이디")
    private String uid;

    @Schema(example = "모카")
    private String nickname;

    @Schema(example = "남성는 true, 여성는 false")
    private Gender gender;

    @Schema(example = "프로필 이미지 사진")
    @Nullable
    private MultipartFile profileImage;

    @Schema(example = "ONE")
    @Nullable
    private AgeGroup ageGroup;
}

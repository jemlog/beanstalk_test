package com.example.beanstalk.common.dataload;


import com.example.beanstalk.auth.presentation.dto.Provider;
import com.example.beanstalk.user.domain.User;
import com.example.beanstalk.user.domain.UserRepository;
import com.example.beanstalk.user.domain.enums.AgeGroup;
import com.example.beanstalk.user.domain.enums.Gender;
import com.example.beanstalk.user.domain.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;


    // TODO : Builder 사용시 엔티티 초기화 되지 않는 문제
    @Transactional
    public void loadData() {
        userRepository.deleteAll();


        // 테스트 유저 입력
        User user = User.builder().uid("cmc11th")
                .email("melly@gmail.com")
                .password("asdfasdf")
                .roleType(RoleType.USER)
                .profileImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/cdc6a8f9-8798-4214-94ae-1e5538944f60.jpg")
                .nickname("떡잎마을방범대")
                .ageGroup(AgeGroup.TWO)
                .provider(Provider.DEFAULT)
                .gender(Gender.MALE)
                .build();
        User saveUser = userRepository.save(user);
    }

}

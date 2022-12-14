package com.example.beanstalk.user.domain;
import com.example.beanstalk.auth.presentation.dto.Provider;
import com.example.beanstalk.common.util.jpa.JpaBaseEntity;
import com.example.beanstalk.user.domain.enums.AgeGroup;
import com.example.beanstalk.user.domain.enums.Gender;
import com.example.beanstalk.user.domain.enums.RoleType;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class User extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    // SocialId or 일반 로그인 시 UUID 추가
    @Column(name = "user_id",nullable = false)
    private String userId;

    private String email;

    private String password;

    private String nickname;

    private String profileImage;

    @ElementCollection
    @CollectionTable(
            name = "visited_place_table",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "visited_place") // 컬럼명 지정 (예외)
    private Set<Long> visitedPlace = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Embedded
    private Recommend recommend;

    @Column(name = "store_capacity")
    private Double storeCapacity;

//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
//    private List<Memory> memories = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
//    private List<GroupAndUser> groupAndUsers = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
//    private List<Scrap> scraps = new ArrayList<>();

    public void updateUser(String nickname, Gender gender, String profileImage, AgeGroup ageGroup)
    {
        this.nickname = nickname;
        this.gender = gender;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
    }


    public User(String uid, Provider provider,RoleType roleType,String password)
    {
        this.userId  = uid;
        this.provider = provider;
        this.roleType = roleType;
        this.password = password;
    }

    @Builder
    public User(String email,String password,RoleType roleType,String profileImage,AgeGroup ageGroup,Gender gender,String uid,Provider provider,String nickname)
    {
        this.email = email;
        this.password = password;
        this.roleType = roleType;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
        this.gender =gender;
        this.userId = uid;
        this.provider = provider;
        this.nickname = nickname;
    }
}

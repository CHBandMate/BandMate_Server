package com.mate.band.domain.user.entity;

import com.mate.band.domain.band.entity.BandApplyInfoEntity;
import com.mate.band.domain.band.entity.BandEntity;
import com.mate.band.domain.band.entity.BandMemberEntity;
import com.mate.band.domain.metadata.entity.DistrictMappingEntity;
import com.mate.band.domain.metadata.entity.MusicGenreMappingEntity;
import com.mate.band.domain.metadata.entity.PositionMappingEntity;
import com.mate.band.domain.user.dto.RegisterUserProfileRequestDTO;
import com.mate.band.global.entity.BaseTimeEntity;
import com.mate.band.global.security.constants.OAuthType;
import com.mate.band.global.security.constants.Role;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * user 테이블 매핑 객체
 * @author : sungmin
 * @fileName : UserEntity
 * @since : 2024/12/17
 */
@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UserEntity extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "oauth_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthType oauthType;

    @Column(name = "oauth_id", unique = true, nullable = false)
    private String oauthId;

    // LEADER : 리더, USER : 일반
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "nickname", length = 50, unique = true)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "email")
    private String email;

    @Column(name = "kakao_id", length = 50)
    private String kakaoId;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "instruments")
    private String instruments;

    @Column(name = "effectors")
    private String effectors;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "expose_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean exposeYn = false;

    @Column(name = "last_login_dt", columnDefinition = "DATETIME", insertable = false)
    private LocalDateTime lastLoginDt;

    @Column(name = "delete_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean deleteYn = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PositionMappingEntity> positions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DistrictMappingEntity> districts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicGenreMappingEntity> musicGenres = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BandEntity> bands = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BandMemberEntity> positionInBands = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BandApplyInfoEntity> bandApplyInfos = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInviteInfoEntity> userInviteInfos = new ArrayList<>();

    public void updateUser(RegisterUserProfileRequestDTO profileParam) {
        this.role = Role.USER;
        this.nickname = profileParam.nickName();
//        this.profileImageUrl = registerProfile.profileImageUrl();
        this.email = profileParam.email();
        this.kakaoId = profileParam.kakaoId();
//        this.phoneNumber = registerProfile.phoneNumber();
        this.instruments = String.join(",", profileParam.instruments());
        this.effectors = String.join(",", profileParam.effectors());
        this.introduction = profileParam.introduction();
        this.exposeYn = profileParam.exposeYn();
    }

    public void updateRole(Role role) {
        this.role = role;
    }

}

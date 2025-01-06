package com.mate.band.domain.user.entity;

import com.mate.band.domain.profile.entity.DistrictMappingEntity;
import com.mate.band.domain.profile.entity.PositionMappingEntity;
import com.mate.band.domain.user.dto.RegisterProfileRequestDTO;
import com.mate.band.global.security.constants.OAuthType;
import com.mate.band.global.security.constants.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
public class UserEntity implements Serializable {

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
    private Boolean exposeYn;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", columnDefinition = "DATETIME", insertable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "last_login_dt", columnDefinition = "DATETIME", insertable = false)
    private LocalDateTime lastLoginDt;

    @Column(name = "delete_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean deleteYn;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PositionMappingEntity> positions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DistrictMappingEntity> regions = new ArrayList<>();


    public void registryUser(RegisterProfileRequestDTO registerProfile) {
        this.role = Role.USER;
        this.nickname = registerProfile.nickName();
//        this.profileImageUrl = registerProfile.profileImageUrl();
        this.email = registerProfile.email();
        this.kakaoId = registerProfile.kakaoId();
//        this.phoneNumber = registerProfile.phoneNumber();
        this.instruments = String.join(",", registerProfile.instruments());
        this.effectors = String.join(",", registerProfile.effectors());
        this.introduction = registerProfile.introduction();
        this.exposeYn = registerProfile.exposeYn();
    }

}

package com.matching.band.domain.user.entity;

import com.matching.band.global.security.constants.OAuthType;
import com.matching.band.global.security.constants.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    @Column(name = "user_no", nullable = false)
    private Long userNo;

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

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "email")
    private String email;

    @Column(name = "kakao_id", length = 50)
    private String kakaoId;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "position", length = 100)
    private String position;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "join_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean joinYn;

    @Column(name = "join_position", length = 100)
    private String joinPosition;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_login_dt", columnDefinition = "DATETIME", insertable = false)
    private LocalDateTime lastLoginDt;

    @Column(name = "delete_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean deleteYn;

}

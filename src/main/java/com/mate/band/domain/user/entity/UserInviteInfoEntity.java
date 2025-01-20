package com.mate.band.domain.user.entity;


import com.mate.band.domain.band.entity.BandEntity;
import com.mate.band.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "user_invite_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UserInviteInfoEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_invite_info_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id", nullable = false)
    private BandEntity band;

    @Column(name = "description")
    private String description;

    @Column(name = "open_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean openYn = false;

    @Column(name = "delete_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean deleteYn = false;

}

package com.mate.band.domain.band.entity;


import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 밴드 가입 지원 정보 Entity
 * @author : 최성민
 * @since : 2025-01-18
 * @version : 1.0
 */
@Getter
@Entity
@Table(name = "band_apply_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class BandApplyInfoEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_apply_info_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id", nullable = false)
    private BandEntity band;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "description")
    private String description;

    @Column(name = "open_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean openYn = false;

    @Column(name = "delete_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean deleteYn = false;

}

package com.mate.band.domain.band.entity;

import com.mate.band.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 밴드 구인 정보 Entity
 * @author : 최성민
 * @since : 2025-01-09
 * @version : 1.0
 */
@Getter
@Entity
@Table(name = "band_recruit_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class BandRecruitInfoEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_recruit_info_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "band_id")
    private BandEntity band;

    @Column(name = "title")
    @Setter
    private String title;

    @Column(name = "recruitDescription", columnDefinition = "TEXT")
    @Setter
    private String description;

}

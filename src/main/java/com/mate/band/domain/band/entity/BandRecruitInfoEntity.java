package com.mate.band.domain.band.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "band_recruit_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class BandRecruitInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_recruit_info_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "band_id")
    private BandEntity band;

    @Column(name = "title")
    private String title;

    @Column(name = "recruitDescription", columnDefinition = "TEXT")
    private String description;

}

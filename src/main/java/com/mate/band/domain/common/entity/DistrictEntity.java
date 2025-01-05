package com.mate.band.domain.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "districts")
@Entity
@Builder
@AllArgsConstructor
public class DistrictEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "district_name", length = 20)
    private String districtName;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false) // FK 설정
    private RegionEntity region;
}

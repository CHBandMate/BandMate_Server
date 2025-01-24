package com.mate.band.domain.metadata.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 지역 정보 Entity
 * @author : 최성민
 * @since : 2025-01-05
 * @version : 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "districts")
@Entity
@Builder
@AllArgsConstructor
public class DistrictEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private Long id;

    @Column(name = "district_name", length = 20)
    private String districtName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private RegionEntity region;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DistrictMappingEntity> districts = new ArrayList<>();

}

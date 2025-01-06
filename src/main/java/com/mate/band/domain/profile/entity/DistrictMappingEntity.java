package com.mate.band.domain.profile.entity;


import com.mate.band.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "district_mapping")
@Entity
@Builder
@AllArgsConstructor
public class DistrictMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_mapping_id")
    private Long id;

    @Column(name = "type")
    private char type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private DistrictEntity district;
}

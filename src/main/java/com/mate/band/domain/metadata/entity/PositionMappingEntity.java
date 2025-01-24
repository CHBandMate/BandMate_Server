package com.mate.band.domain.metadata.entity;

import com.mate.band.domain.band.entity.BandEntity;
import com.mate.band.domain.metadata.constants.MappingType;
import com.mate.band.domain.metadata.constants.Position;
import com.mate.band.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 밴드 포지션 <-> 회원,밴드 매핑 Entity</br>
 * Band -> 모집 포지션</br>
 * User -> 본인 소개 포지션</br>
 * @author : 최성민
 * @since : 2025-01-24
 * @version : 1.0
 */
@Getter
@Entity
@Table(name = "position_mapping")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PositionMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_mapping_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MappingType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private BandEntity band;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;

}

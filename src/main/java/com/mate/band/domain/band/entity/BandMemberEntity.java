package com.mate.band.domain.band.entity;

import com.mate.band.domain.metadata.constants.Position;
import com.mate.band.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 밴드 멤버 Entity
 * @author : 최성민
 * @since : 2025-01-09
 * @version : 1.0
 */
@Getter
@Entity
@Table(name = "band_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class BandMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_member_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id", nullable = false)
    private BandEntity band;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private Position position;

}

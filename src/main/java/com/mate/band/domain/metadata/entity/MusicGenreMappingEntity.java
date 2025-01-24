package com.mate.band.domain.metadata.entity;

import com.mate.band.domain.band.entity.BandEntity;
import com.mate.band.domain.metadata.constants.MappingType;
import com.mate.band.domain.metadata.constants.MusicGenre;
import com.mate.band.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 음악 장르 <-> 회원,밴드 매핑 Entity
 * @author : 최성민
 * @since : 2025-01-07
 * @version : 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "music_genre_mapping")
@Entity
@Builder
@AllArgsConstructor
public class MusicGenreMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_genre_mapping_id")
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
    @Column(name = "genre")
    private MusicGenre genre;
}

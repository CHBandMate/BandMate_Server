package com.mate.band.domain.band.entity;


import com.mate.band.domain.profile.entity.DistrictMappingEntity;
import com.mate.band.domain.profile.entity.MusicGenreMappingEntity;
import com.mate.band.domain.profile.entity.PositionMappingEntity;
import com.mate.band.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "band")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class BandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "band_name", length = 100, nullable = false)
    private String bandName;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "expose_yn", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean exposeYn;

    @Column(name = "recruit_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean recruitYn;

    @Column(name = "delete_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean deleteYn;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DistrictMappingEntity> districts = new ArrayList<>();

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicGenreMappingEntity> musicGenres = new ArrayList<>();

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PositionMappingEntity> recruitingPositions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", columnDefinition = "DATETIME", insertable = false)
    private LocalDateTime modifiedAt;

    @OneToOne(mappedBy = "band")
    private BandRecruitInfoEntity bandRecruitInfoEntity;

}

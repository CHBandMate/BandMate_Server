package com.mate.band.domain.band.entity;


import com.mate.band.domain.band.dto.RegisterBandProfileRequestDTO;
import com.mate.band.domain.metadata.entity.DistrictMappingEntity;
import com.mate.band.domain.metadata.entity.MusicGenreMappingEntity;
import com.mate.band.domain.metadata.entity.PositionMappingEntity;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.entity.UserInviteInfoEntity;
import com.mate.band.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "band")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class BandEntity extends BaseTimeEntity {

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

    @Column(name = "recruit_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean recruitYn;

    @Column(name = "expose_yn", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean exposeYn;

    @Column(name = "delete_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean deleteYn = false;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DistrictMappingEntity> districts = new ArrayList<>();

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicGenreMappingEntity> musicGenres = new ArrayList<>();

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PositionMappingEntity> recruitingPositions = new ArrayList<>();

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BandMemberEntity> bandMembers = new ArrayList<>();

    @OneToOne(mappedBy = "band")
    @Setter
    private BandRecruitInfoEntity bandRecruitInfoEntity;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BandApplyInfoEntity> bandApplyInfos = new ArrayList<>();

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInviteInfoEntity> userInviteInfos = new ArrayList<>();

    public void updateBand(RegisterBandProfileRequestDTO profileParam) {
        this.bandName = profileParam.bandName();
        this.introduction = profileParam.introduction();
        this.recruitYn = profileParam.recruitYn();
        this.exposeYn = profileParam.exposeYn();
    }

}

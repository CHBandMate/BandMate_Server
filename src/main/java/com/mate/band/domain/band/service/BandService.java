package com.mate.band.domain.band.service;

import com.mate.band.domain.band.dto.BandMemberDTO;
import com.mate.band.domain.band.dto.RegisterBandProfileRequestDTO;
import com.mate.band.domain.band.entity.BandEntity;
import com.mate.band.domain.band.entity.BandMemberEntity;
import com.mate.band.domain.band.entity.BandRecruitInfoEntity;
import com.mate.band.domain.band.repository.BandMemberEntityRepository;
import com.mate.band.domain.band.repository.BandRecruitInfoRepository;
import com.mate.band.domain.band.repository.BandRepository;
import com.mate.band.domain.profile.constants.MappingType;
import com.mate.band.domain.profile.constants.MusicGenre;
import com.mate.band.domain.profile.constants.Position;
import com.mate.band.domain.profile.entity.DistrictEntity;
import com.mate.band.domain.profile.entity.DistrictMappingEntity;
import com.mate.band.domain.profile.entity.MusicGenreMappingEntity;
import com.mate.band.domain.profile.entity.PositionMappingEntity;
import com.mate.band.domain.profile.repository.*;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.repository.UserRepository;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BandService {

    private final BandRepository bandRepository;
    private final UserRepository userRepository;
    private final BandMemberEntityRepository bandMemberEntityRepository;
    private final BandRecruitInfoRepository bandRecruitInfoRepository;
    private final DistrictRepository districtRepository;
    private final PositionMappingRepository positionMappingRepository;
    private final MusicGenreMappingRepository musicGenreMappingRepository;
    private final DistrictMappingRepository districtMappingRepository;

    @Transactional
    public void registerBandProfile(UserEntity user, RegisterBandProfileRequestDTO profileParam) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        MetadataEnumRepository.verifyMetadataKey(profileParam.genre(), MusicGenre.class);
        List<DistrictEntity> districts = verifyDistrict(profileParam.district());

        BandEntity bandEntity =
                BandEntity.builder()
                        .user(userEntity)
                        .bandName(profileParam.bandName())
                        .introduction(profileParam.introduction())
                        .exposeYn(profileParam.exposeYn())
                        .recruitYn(profileParam.recruitYn())
                        .build();

        List<MusicGenreMappingEntity> musicGenreMappingEntityList = profileParam.genre().stream().map(genre ->
                MusicGenreMappingEntity.builder()
                        .type(MappingType.BAND)
                        .band(bandEntity)
                        .genre(MusicGenre.valueOf(genre))
                        .build()).toList();

        List<DistrictMappingEntity> districtMappingEntityList = districts.stream().map(district ->
                DistrictMappingEntity.builder()
                        .type(MappingType.BAND)
                        .band(bandEntity)
                        .district(district)
                        .build()).toList();

        bandRepository.save(bandEntity);
        musicGenreMappingRepository.saveAll(musicGenreMappingEntityList);
        districtMappingRepository.saveAll(districtMappingEntityList);

        // 밴드 멤버 저장
        if (!profileParam.bandMember().isEmpty()) {
            List<BandMemberEntity> bandMemberEntityList = new ArrayList<>();
            for (BandMemberDTO bandMember : profileParam.bandMember()) {
                UserEntity member;
                if (bandMember.userId() == userEntity.getId()) {    // 나 자신 일때
                    member = userEntity;
                } else {
                    member = userRepository.findById(bandMember.userId()).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
                }
                BandMemberEntity.builder().band(bandEntity).user(member).position(Position.valueOf(bandMember.positionCode())).build();
            }
            bandMemberEntityRepository.saveAll(bandMemberEntityList);
        }

        // 구인정보 저장
        if (profileParam.recruitYn()) {
            // 구인 포지션
            MetadataEnumRepository.verifyMetadataKey(profileParam.recruitPosition(), Position.class);
            List<PositionMappingEntity> positionMappingEntityList = profileParam.recruitPosition().stream().map(position ->
                    PositionMappingEntity.builder()
                            .type(MappingType.BAND)
                            .band(bandEntity)
                            .position(Position.valueOf(position))
                            .build()).toList();
            positionMappingRepository.saveAll(positionMappingEntityList);

            // 구인 내용
            BandRecruitInfoEntity recruitInfoEntity =
                    BandRecruitInfoEntity.builder()
                            .band(bandEntity)
                            .title(profileParam.recruitTitle())
                            .description(profileParam.recruitDescription())
                            .build();
            bandRecruitInfoRepository.save(recruitInfoEntity);
        }
    }

    private List<DistrictEntity> verifyDistrict(List<Long> districts) {
        List<DistrictEntity> districtEntityList = districtRepository.findByIdIn(districts);
        if (districts.size() != districtEntityList.size()) {
            throw new BusinessException(ErrorCode.NOT_EXIST_CODE);
        }
        return districtEntityList;
    }

}

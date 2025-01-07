package com.mate.band.domain.user.service;

import com.mate.band.domain.profile.constants.MappingType;
import com.mate.band.domain.profile.constants.Metadata;
import com.mate.band.domain.profile.entity.*;
import com.mate.band.domain.profile.repository.*;
import com.mate.band.domain.user.dto.RegisterProfileRequestDTO;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.repository.UserRepository;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.security.constants.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final DistrictRepository districtRepository;
    private final PositionMappingRepository positionMappingRepository;
    private final MusicGenreMappingRepository musicGenreMappingRepository;
    private final DistrictMappingRepository districtMappingRepository;

    // TODO 예외처리, 리팩토링 필요
    @Transactional
    public void registerProfile(UserEntity user, RegisterProfileRequestDTO registerProfileRequest) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException("존재하지 않는 회원"));
        if (userEntity.getRole() != Role.NOT_REGISTERED) {
            throw new BusinessException("이미 등록 된 회원입니다.");
        }

        List<CommonCodeEntity> positions = verifyCommonCode(registerProfileRequest.position(), Metadata.BAND_POSITION);
        List<CommonCodeEntity> genres = verifyCommonCode(registerProfileRequest.genre(), Metadata.MUSIC_GENRE);
        List<DistrictEntity> districts = verifyDistrict(registerProfileRequest.district());

        userEntity.registryUser(registerProfileRequest);

        List<PositionMappingEntity> positionMappingEntityList = positions.stream().map(position ->
                PositionMappingEntity.builder()
                        .type(MappingType.USER)
                        .user(userEntity)
                        .position(position)
                        .build()).toList();

        List<MusicGenreMappingEntity> musicGenreMappingEntityList = genres.stream().map(genre ->
                MusicGenreMappingEntity.builder()
                        .type(MappingType.USER)
                        .user(userEntity)
                        .genre(genre)
                        .build()).toList();

        List<DistrictMappingEntity> districtMappingEntityList = districts.stream().map(district ->
                DistrictMappingEntity.builder()
                        .type(MappingType.USER)
                        .user(userEntity)
                        .district(district)
                        .build()).toList();

        positionMappingRepository.saveAll(positionMappingEntityList);
        musicGenreMappingRepository.saveAll(musicGenreMappingEntityList);
        districtMappingRepository.saveAll(districtMappingEntityList);
    }

    private List<CommonCodeEntity> verifyCommonCode(List<String> codeList, Metadata metadata) {
        List<CommonCodeEntity> codeEntityList = commonCodeRepository.findByCodeIn(metadata.getCodeGroup(), codeList);
        if (codeList.size() != codeEntityList.size()) {
            throw new BusinessException("유효 하지 않은 코드가 포함 되어 있음");
        }
        return codeEntityList;
    }

    private List<DistrictEntity> verifyDistrict(List<Long> districts) {
        List<DistrictEntity> districtEntityList = districtRepository.findByIdIn(districts);
        if (districts.size() != districtEntityList.size()) {
            throw new BusinessException("존재 하지 않는 지역 코드가 포함");
        }
        return districtEntityList;
    }

}

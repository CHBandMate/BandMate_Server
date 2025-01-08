package com.mate.band.domain.user.service;

import com.mate.band.domain.common.EnumModel;
import com.mate.band.domain.profile.constants.MappingType;
import com.mate.band.domain.profile.constants.MusicGenre;
import com.mate.band.domain.profile.constants.Position;
import com.mate.band.domain.profile.entity.DistrictEntity;
import com.mate.band.domain.profile.entity.DistrictMappingEntity;
import com.mate.band.domain.profile.entity.MusicGenreMappingEntity;
import com.mate.band.domain.profile.entity.PositionMappingEntity;
import com.mate.band.domain.profile.repository.DistrictMappingRepository;
import com.mate.band.domain.profile.repository.DistrictRepository;
import com.mate.band.domain.profile.repository.MusicGenreMappingRepository;
import com.mate.band.domain.profile.repository.PositionMappingRepository;
import com.mate.band.domain.user.dto.RegisterProfileRequestDTO;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.repository.UserRepository;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.exception.ErrorCode;
import com.mate.band.global.security.constants.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DistrictRepository districtRepository;
    private final PositionMappingRepository positionMappingRepository;
    private final MusicGenreMappingRepository musicGenreMappingRepository;
    private final DistrictMappingRepository districtMappingRepository;

    // TODO 리팩토링
    @Transactional
    public void registerProfile(UserEntity user, RegisterProfileRequestDTO registerProfileRequest) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        if (userEntity.getRole() != Role.NOT_REGISTERED) {
            throw new BusinessException(ErrorCode.REGISTERED_USER);
        }

        verifyMetadataKey(registerProfileRequest.position(), Position.class);
        verifyMetadataKey(registerProfileRequest.genre(), MusicGenre.class);
        List<DistrictEntity> districts = verifyDistrict(registerProfileRequest.district());
        userEntity.registryUser(registerProfileRequest);

        List<PositionMappingEntity> positionMappingEntityList = registerProfileRequest.position().stream().map(position ->
                PositionMappingEntity.builder()
                        .type(MappingType.USER)
                        .user(userEntity)
                        .position(Position.valueOf(position))
                        .build()).toList();

        List<MusicGenreMappingEntity> musicGenreMappingEntityList = registerProfileRequest.genre().stream().map(genre ->
                MusicGenreMappingEntity.builder()
                        .type(MappingType.USER)
                        .user(userEntity)
                        .genre(MusicGenre.valueOf(genre))
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

    private void verifyMetadataKey(List<String> keyList, Class<? extends EnumModel> enumClass) {
        EnumModel[] enumConstants = enumClass.getEnumConstants();
        for (String key : keyList) {
            boolean isValid = false;
            for (EnumModel enumConstant : enumConstants) {
                if (enumConstant.getkey().equals(key)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                throw new BusinessException(ErrorCode.NOT_EXIST_CODE);
            }
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

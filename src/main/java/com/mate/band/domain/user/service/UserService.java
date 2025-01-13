package com.mate.band.domain.user.service;

import com.mate.band.domain.metadata.constants.MappingType;
import com.mate.band.domain.metadata.constants.MusicGenre;
import com.mate.band.domain.metadata.constants.Position;
import com.mate.band.domain.metadata.entity.DistrictEntity;
import com.mate.band.domain.metadata.entity.DistrictMappingEntity;
import com.mate.band.domain.metadata.entity.MusicGenreMappingEntity;
import com.mate.band.domain.metadata.entity.PositionMappingEntity;
import com.mate.band.domain.metadata.repository.*;
import com.mate.band.domain.user.dto.RegisterUserProfileRequestDTO;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.repository.UserRepository;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.exception.ErrorCode;
import com.mate.band.global.security.constants.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DistrictRepository districtRepository;
    private final PositionMappingRepository positionMappingRepository;
    private final MusicGenreMappingRepository musicGenreMappingRepository;
    private final DistrictMappingRepository districtMappingRepository;

    public Optional<UserEntity> findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    // TODO 리팩토링
    @Transactional
    public void registerUserProfile(UserEntity user, RegisterUserProfileRequestDTO profileParam) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        if (userEntity.getRole() != Role.NOT_REGISTERED) {
            throw new BusinessException(ErrorCode.REGISTERED_USER);
        }

        MetadataEnumRepository.verifyMetadataKey(profileParam.position(), Position.class);
        MetadataEnumRepository.verifyMetadataKey(profileParam.genre(), MusicGenre.class);
        List<DistrictEntity> districts = verifyDistrict(profileParam.district());
        userEntity.registryUser(profileParam);

        List<PositionMappingEntity> positionMappingEntityList = profileParam.position().stream().map(position ->
                PositionMappingEntity.builder()
                        .type(MappingType.USER)
                        .user(userEntity)
                        .position(Position.valueOf(position))
                        .build()).toList();

        List<MusicGenreMappingEntity> musicGenreMappingEntityList = profileParam.genre().stream().map(genre ->
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

    private List<DistrictEntity> verifyDistrict(List<Long> districts) {
        List<DistrictEntity> districtEntityList = districtRepository.findByIdIn(districts);
        if (districts.size() != districtEntityList.size()) {
            throw new BusinessException(ErrorCode.NOT_EXIST_CODE);
        }
        return districtEntityList;
    }

}

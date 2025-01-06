package com.mate.band.domain.user.service;

import com.mate.band.domain.profile.constants.MappingType;
import com.mate.band.domain.profile.constants.Metadata;
import com.mate.band.domain.profile.entity.DistrictMappingEntity;
import com.mate.band.domain.profile.entity.PositionMappingEntity;
import com.mate.band.domain.profile.repository.CommonCodeRepository;
import com.mate.band.domain.profile.repository.DistrictMappingRepository;
import com.mate.band.domain.profile.repository.DistrictRepository;
import com.mate.band.domain.profile.repository.PositionMappingRepository;
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
    private final DistrictMappingRepository districtMappingRepository;

    @Transactional
    public void registerProfile(UserEntity user, RegisterProfileRequestDTO registerProfileRequest) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException("존재하지 않는 회원"));
        if (userEntity.getRole() != Role.NOT_REGISTERED) {
            throw new BusinessException("이미 등록 된 회원입니다.");
        }
        userEntity.registryUser(registerProfileRequest);

        List<PositionMappingEntity> positionEntityList = registerProfileRequest.position().stream().map(position ->
                PositionMappingEntity.builder()
                        .type(MappingType.USER.getValue())
                        .user(userEntity)
                        .position(commonCodeRepository.findByCodeInfo(Metadata.BAND_POSITION.getCodeGroup(), position))
                        .build()).toList();

        List<DistrictMappingEntity> districtEntityList = registerProfileRequest.district().stream().map(district ->
                DistrictMappingEntity.builder()
                        .type(MappingType.USER.getValue())
                        .user(userEntity)
                        .district(districtRepository.findById(Long.valueOf(district)).get())
                        .build()).toList();

        positionMappingRepository.saveAll(positionEntityList);
        districtMappingRepository.saveAll(districtEntityList);
    }

}

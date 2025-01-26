package com.mate.band.domain.metadata.service;

import com.mate.band.domain.common.EnumModel;
import com.mate.band.domain.metadata.constants.MusicGenre;
import com.mate.band.domain.metadata.constants.Position;
import com.mate.band.domain.metadata.constants.SnsPlatform;
import com.mate.band.domain.metadata.dto.*;
import com.mate.band.domain.metadata.entity.DistrictEntity;
import com.mate.band.domain.metadata.repository.DistrictRepository;
import com.mate.band.domain.metadata.repository.RegionRepository;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author : 최성민
 * @since : 2025-01-06
 * @version : 1.0
 */
@Service
@RequiredArgsConstructor
public class ProfileMetadataService {

    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;

    /**
     * ENUM에 등록 된 모든 음악 장르, 밴드 포지션, SNS 플랫폼 항목을 조회합니다.
     * @return ProfileMetaDataResponseDTO
     */
    public ProfileMetaDataResponseDTO getProfileMetadata() {
        List<ProfileMetaDataDTO> musicGenreList = getProfileMetaDataDTOList(MusicGenre.class);
        List<ProfileMetaDataDTO> positionList = getProfileMetaDataDTOList(Position.class);
        List<ProfileMetaDataDTO> snsPlatformList = getProfileMetaDataDTOList(SnsPlatform.class);

        return ProfileMetaDataResponseDTO.builder()
                .musicGenre(musicGenreList)
                .position(positionList)
                .snsPlatform(snsPlatformList)
                .build();
    }

    /**
     * 지역 데이터를 검증한다.
     * @param districts 지역 정보
     * @return List DistrictEntity
     * @throws BusinessException 존재하지 않는 지역 코드
     */
    public List<DistrictEntity> verifyDistrict(List<Long> districts) {
        List<DistrictEntity> districtEntityList = districtRepository.findByIdIn(districts);
        if (districts.size() != districtEntityList.size()) {
            throw new BusinessException(ErrorCode.NOT_EXIST_CODE);
        }
        return districtEntityList;
    }

    /**
     * DB에 저장 된 지역 항목을 조회합니다.
     * @return List RegionResponseDTO
     */
    public List<RegionResponseDTO> getDistrictData() {
        List<RegionDataDTO> regions = regionRepository.getRegionsWithDistricts();

        Map<Integer, RegionResponseDTO> regionResponseMap = new HashMap<>();
        for (RegionDataDTO regionData : regions) {

            long regionId = regionData.regionId();
            String regionName = regionData.regionName();
            long districtId = regionData.districtId();
            String districtName = regionData.districtName();

            RegionResponseDTO regionResponse =
                    regionResponseMap.computeIfAbsent((int) regionId, id ->
                            RegionResponseDTO.builder()
                                    .regionId(regionId)
                                    .regionName(regionName)
                                    .districts(new ArrayList<>())
                                    .build()
                    );

            regionResponse.districts().add(
                    DistrictDataDTO.builder()
                            .districtId(districtId)
                            .districtName(districtName)
                            .build()
            );
        }

        return new ArrayList<>(regionResponseMap.values());
    }

    /**
     * ENUM 클래스를 List 형식으로 변환합니다.
     * @return List ProfileMetaDataDTO
     */
    private List<ProfileMetaDataDTO> getProfileMetaDataDTOList(Class<? extends EnumModel> profileMetadataEnum) {
        return Stream.of(profileMetadataEnum.getEnumConstants())
                .map(metadata ->
                        ProfileMetaDataDTO.builder()
                                .key(metadata.getkey())
                                .value(metadata.getValue())
                                .build()
                ).toList();
    }

}

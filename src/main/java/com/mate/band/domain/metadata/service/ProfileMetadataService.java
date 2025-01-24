package com.mate.band.domain.metadata.service;

import com.mate.band.domain.common.EnumModel;
import com.mate.band.domain.metadata.constants.MusicGenre;
import com.mate.band.domain.metadata.constants.Position;
import com.mate.band.domain.metadata.constants.SnsPlatform;
import com.mate.band.domain.metadata.dto.*;
import com.mate.band.domain.metadata.repository.RegionRepository;
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


    /**
     * ENUM에 등록 된 음악 장르, 밴드 포지션, SNS 플랫폼 항목을 조회합니다.
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

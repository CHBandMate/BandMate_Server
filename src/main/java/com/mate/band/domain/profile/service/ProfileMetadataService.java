package com.mate.band.domain.profile.service;

import com.mate.band.domain.common.EnumModel;
import com.mate.band.domain.profile.constants.MusicGenre;
import com.mate.band.domain.profile.constants.Position;
import com.mate.band.domain.profile.constants.SnsPlatform;
import com.mate.band.domain.profile.dto.*;
import com.mate.band.domain.profile.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProfileMetadataService {

    private final RegionRepository regionRepository;

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

    public List<RegionResponseDTO> getRegionData() {
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

    private List<ProfileMetaDataDTO> getProfileMetaDataDTOList(Class<? extends EnumModel> profileMetadataEnum) {
        return Stream.of(profileMetadataEnum.getEnumConstants())
                .map(musicGenre ->
                        ProfileMetaDataDTO.builder()
                                .key(musicGenre.getkey())
                                .value(musicGenre.getValue())
                                .build()
                ).toList();
    }

}

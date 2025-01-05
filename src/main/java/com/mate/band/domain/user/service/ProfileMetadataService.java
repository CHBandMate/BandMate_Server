package com.mate.band.domain.user.service;

import com.mate.band.domain.common.dto.DistrictData;
import com.mate.band.domain.common.dto.RegionData;
import com.mate.band.domain.common.dto.RegionResponse;
import com.mate.band.domain.common.repository.CommonCodeRepository;
import com.mate.band.domain.common.repository.RegionRepository;
import com.mate.band.domain.user.constants.ProfileMetadata;
import com.mate.band.domain.user.dto.MetaDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfileMetadataService {

    private final CommonCodeRepository commonCodeRepository;
    private final RegionRepository regionRepository;

    public MetaDataResponse getProfileMetadata() {
        return MetaDataResponse.builder()
                .musicGenre(commonCodeRepository.findValueByCodeGroup(ProfileMetadata.MUSIC_GENRE.getCodeGroup()))
                .position(commonCodeRepository.findValueByCodeGroup(ProfileMetadata.BAND_POSITION.getCodeGroup()))
                .snsPlatform(commonCodeRepository.findValueByCodeGroup(ProfileMetadata.SNS_PLATFORM.getCodeGroup()))
                .build();
    }

    public List<RegionResponse> getRegionData() {
        List<RegionData> regions = regionRepository.getRegionsWithDistricts();

        Map<Integer, RegionResponse> regionResponseMap = new HashMap<>();
        for (RegionData regionData : regions) {

            long regionId = regionData.regionId();
            String regionName = regionData.regionName();
            long districtId = regionData.districtId();
            String districtName = regionData.districtName();

            RegionResponse regionResponse =
                    regionResponseMap.computeIfAbsent((int) regionId, id ->
                            RegionResponse.builder()
                                    .regionId(regionId)
                                    .regionName(regionName)
                                    .districts(new ArrayList<>())
                                    .build()
                    );

            regionResponse.districts().add(DistrictData.builder().districtId(districtId).districtName(districtName).build());
        }

        return new ArrayList<>(regionResponseMap.values());
    }

}

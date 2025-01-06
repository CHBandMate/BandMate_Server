package com.mate.band.domain.profile.service;

import com.mate.band.domain.profile.dto.DistrictDataDTO;
import com.mate.band.domain.profile.dto.RegionDataDTO;
import com.mate.band.domain.profile.dto.RegionResponseDTO;
import com.mate.band.domain.profile.repository.CommonCodeRepository;
import com.mate.band.domain.profile.repository.RegionRepository;
import com.mate.band.domain.profile.constants.Metadata;
import com.mate.band.domain.profile.dto.ProfileMetaDataResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfileMetadataService {

    private final CommonCodeRepository commonCodeRepository;
    private final RegionRepository regionRepository;

    public ProfileMetaDataResponseDTO getProfileMetadata() {
        return ProfileMetaDataResponseDTO.builder()
                .musicGenre(commonCodeRepository.findValueByCodeGroup(Metadata.MUSIC_GENRE.getCodeGroup()))
                .position(commonCodeRepository.findValueByCodeGroup(Metadata.BAND_POSITION.getCodeGroup()))
                .snsPlatform(commonCodeRepository.findValueByCodeGroup(Metadata.SNS_PLATFORM.getCodeGroup()))
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

            regionResponse.districts().add(DistrictDataDTO.builder().districtId(districtId).districtName(districtName).build());
        }

        return new ArrayList<>(regionResponseMap.values());
    }

}

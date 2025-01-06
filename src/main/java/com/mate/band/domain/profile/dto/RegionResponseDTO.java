package com.mate.band.domain.profile.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RegionResponseDTO(
        long regionId,
        String regionName,
        List<DistrictDataDTO> districts
) {
}

package com.mate.band.domain.common.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RegionResponse(
        long regionId,
        String regionName,
        List<DistrictData> districts
) {
}

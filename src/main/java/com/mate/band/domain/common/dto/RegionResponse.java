package com.mate.band.domain.common.dto;

import java.util.List;

public record RegionResponse(
        long regionId,
        String regionName,
        List<DistrictData> districts
) {
}

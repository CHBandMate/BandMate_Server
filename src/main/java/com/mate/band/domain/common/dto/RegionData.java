package com.mate.band.domain.common.dto;

public record RegionData(
        long regionId,
        String regionName,
        long districtId,
        String districtName
) {
}

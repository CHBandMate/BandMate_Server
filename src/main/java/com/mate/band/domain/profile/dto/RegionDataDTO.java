package com.mate.band.domain.profile.dto;

public record RegionDataDTO(
        long regionId,
        String regionName,
        long districtId,
        String districtName
) {
}

package com.mate.band.domain.metadata.dto;

public record RegionDataDTO(
        long regionId,
        String regionName,
        long districtId,
        String districtName
) {
}

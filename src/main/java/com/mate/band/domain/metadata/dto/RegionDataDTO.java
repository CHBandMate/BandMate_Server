package com.mate.band.domain.metadata.dto;

/**
 * RegionRepository 조회용 지역 정보 DTO
 * @author : 최성민
 * @since : 2025-01-06
 * @version : 1.0
 */
public record RegionDataDTO(
        long regionId,
        String regionName,
        long districtId,
        String districtName
) {
}

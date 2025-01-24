package com.mate.band.domain.metadata.dto;

import lombok.Builder;

import java.util.List;

/**
 * 지역 정보 ResponseDTO
 * @author : 최성민
 * @since : 2025-01-06
 * @version : 1.0
 */
@Builder
public record RegionResponseDTO(
        long regionId,
        String regionName,
        List<DistrictDataDTO> districts
) {
}

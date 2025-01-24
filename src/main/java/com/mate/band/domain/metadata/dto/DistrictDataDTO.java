package com.mate.band.domain.metadata.dto;

import lombok.Builder;

/**
 * 지역 데이터 DTO
 * @author : 최성민
 * @since : 2025-01-06
 * @version : 1.0
 */
@Builder
public record DistrictDataDTO(
        long districtId,
        String districtName
) {
}

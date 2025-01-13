package com.mate.band.domain.metadata.dto;

import lombok.Builder;

@Builder
public record DistrictDataDTO(
        long districtId,
        String districtName
) {
}

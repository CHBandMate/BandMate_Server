package com.mate.band.domain.common.dto;

import lombok.Builder;

@Builder
public record DistrictData(
        long districtId,
        String districtName
) {
}

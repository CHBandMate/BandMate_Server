package com.mate.band.domain.profile.dto;

import lombok.Builder;

@Builder
public record DistrictDataDTO(
        long districtId,
        String districtName
) {
}

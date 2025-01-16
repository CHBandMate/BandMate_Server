package com.mate.band.domain.user.dto;

import lombok.Builder;

@Builder
public record UserProfileBandInfoDTO(
        long bandId,
        String bandName,
        String profileImageUrl
) {
}

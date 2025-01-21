package com.mate.band.domain.band.dto;

import lombok.Builder;

@Builder
public record BandApplicantResponseDTO(
        long userId,
        String profileImageUrl,
        String nickname,
        String description,
        boolean openYn
) {
}

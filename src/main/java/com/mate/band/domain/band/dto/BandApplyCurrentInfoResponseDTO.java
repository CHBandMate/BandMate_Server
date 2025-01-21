package com.mate.band.domain.band.dto;

import lombok.Builder;

@Builder
public record BandApplyCurrentInfoResponseDTO(
        long bandId,
        String profileImageUrl,
        String bandName,
        boolean openYn
) {
}

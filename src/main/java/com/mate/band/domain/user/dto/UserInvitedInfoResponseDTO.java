package com.mate.band.domain.user.dto;

import lombok.Builder;

@Builder
public record UserInvitedInfoResponseDTO(
        long bandId,
        String profileImageUrl,
        String bandName,
        boolean openYn
) {
}

package com.mate.band.domain.user.dto;

import lombok.Builder;

@Builder
public record FindUserResponseDTO(
        long userId,
        String nickname
) {
}

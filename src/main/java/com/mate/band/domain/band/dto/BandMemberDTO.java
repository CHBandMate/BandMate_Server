package com.mate.band.domain.band.dto;

import lombok.Builder;

@Builder
public record BandMemberDTO(
        long userId,
        String nickname,
        String position
) {

}

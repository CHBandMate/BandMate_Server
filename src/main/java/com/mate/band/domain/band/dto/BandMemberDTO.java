package com.mate.band.domain.band.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BandMemberDTO(
        @Schema(description = "포지션 코드", example = "BASS")
        String positionCode,
        @Schema(description = "회원 번호", example = "1")
        long userId
) {
}

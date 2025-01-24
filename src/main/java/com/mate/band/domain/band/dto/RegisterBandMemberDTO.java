package com.mate.band.domain.band.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 밴드 멤버 등록 DTO
 * @author : 최성민
 * @since : 2025-01-16
 * @version : 1.0
 */
public record RegisterBandMemberDTO(
        @Schema(description = "포지션 코드", example = "BASS")
        String positionCode,
        @Schema(description = "회원 번호", example = "1")
        long userId
) {
}

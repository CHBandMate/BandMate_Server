package com.mate.band.domain.band.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 밴드 멤버 간략 정보 DTO
 * @author : 최성민
 * @since : 2025-01-10
 * @version : 1.0
 */
@Builder
public record BandMemberDTO(
        @Schema(description = "유저 id", example = "1")
        long userId,

        @Schema(description = "닉네임", example = "베짱이")
        String nickname,

        @Schema(description = "포지션", example = "베이스")
        String position
) {

}

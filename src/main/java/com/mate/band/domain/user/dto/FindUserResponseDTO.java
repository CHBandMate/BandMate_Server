package com.mate.band.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 닉네임으로 회원 검색 ResponseDTO
 * @author : 최성민
 * @since : 2025-01-10
 * @version : 1.0
 */
@Builder
public record FindUserResponseDTO(
        @Schema(description = "회원Id", example = "3")
        long userId,

        @Schema(description = "닉네임", example = "베짱이")
        String nickname
) {
}

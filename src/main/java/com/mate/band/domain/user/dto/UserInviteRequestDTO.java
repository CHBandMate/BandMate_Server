package com.mate.band.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 밴드에 회원 초대 RequestDTO
 * @author : 최성민
 * @since : 2025-01-18
 * @version : 1.0
 */
public record UserInviteRequestDTO(
        @Schema(description = "밴드 id", example = "1")
        long bandId,

        @Schema(description = "회원Id", example = "3")
        long userId,

        @Schema(description = "초대 내용", example = "안녕하세요. 지금 당장 우리 밴드에 들어오시죠!")
        String description
) {
}

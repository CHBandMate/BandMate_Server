package com.mate.band.domain.band.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 밴드 지원자 ResponseDTO
 * @author : 최성민
 * @since : 2025-01-21
 * @version : 1.0
 */
@Builder
public record BandApplicantResponseDTO(
        @Schema(description = "유저 id", example = "3")
        long userId,

        @Schema(description = "프로필 이미지 url")
        String profileImageUrl,

        @Schema(description = "닉네임", example = "베짱이")
        String nickname,

        @Schema(description = "지원 내용", example = "안녕하세요. 밴드에 가입하고 싶습니다!")
        String applyDescription,

        @Schema(description = "열람 여부", example = "false")
        boolean openYn
) {
}

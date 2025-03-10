package com.mate.band.domain.band.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 내가 지원한 밴드 내역 정보 ResponseDTO
 * @author : 최성민
 * @since : 2025-01-21
 * @version : 1.0
 */
@Builder
public record BandApplyCurrentInfoResponseDTO(
        @Schema(description = "밴드 id", example = "1")
        long bandId,

        @Schema(description = "프로필 이미지 url")
        String profileImageUrl,

        @Schema(description = "밴드명", example = "베짱이와 쩌리들")
        String bandName,

        @Schema(description = "열람 여부", example = "false")
        boolean openYn
) {
}

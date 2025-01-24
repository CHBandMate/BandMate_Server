package com.mate.band.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 회원 프로필에 등록 되어 있는 밴드 정보 DTO
 * @author : 최성민
 * @since : 2025-01-16
 * @version : 1.0
 */
@Builder
public record UserProfileBandInfoDTO(
        @Schema(description = "밴드 id", example = "1")
        long bandId,

        @Schema(description = "밴드명", example = "베짱이와 쩌리들")
        String bandName,

        @Schema(description = "프로필 이미지 url")
        String profileImageUrl
) {
}

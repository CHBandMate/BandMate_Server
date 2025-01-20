package com.mate.band.domain.band.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record BandApplyRequestDTO(
        @Schema(description = "밴드 id", example = "1")
        long bandId,
        @Schema(description = "지원 내용", example = "안녕하세요. 밴드에 가입하고 싶습니다!")
        String applyDescription
) {
}

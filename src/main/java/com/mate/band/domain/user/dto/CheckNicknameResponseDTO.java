package com.mate.band.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CheckNicknameResponseDTO(
        @Schema(description = "중복 여부 (true : 중복 O / false : 중복 X)", example = "true")
        boolean duplicated
) {
}

package com.mate.band.global.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenRequestDTO(
        @Schema(description = "임시 코드", example = "1234-asdf-5678-zxcv")
        String authTempCode,

        @Schema(description = "식별자", example = "1")
        long identifier
) {
}

package com.mate.band.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProfileSNSUrlDTO(
        @Schema(description = "SNS 플랫폼 코드", example = "instagram")
        String snsCode,
        @Schema(description = "SNS 플랫폼 계정 or url", example = "zxcv1234")
        String snsId
) {}

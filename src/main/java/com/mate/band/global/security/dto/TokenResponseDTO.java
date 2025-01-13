package com.mate.band.global.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

public record TokenResponseDTO(
        @Schema(description = "회원 등급", example = "ROLE_NOT_REGISTERED")
        String role
) {}

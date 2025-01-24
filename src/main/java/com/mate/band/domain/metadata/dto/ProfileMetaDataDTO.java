package com.mate.band.domain.metadata.dto;

import lombok.Builder;

/**
 * 프로필 메타데이터 형식 DTO
 * @author : 최성민
 * @since : 2025-01-08
 * @version : 1.0
 */
@Builder
public record ProfileMetaDataDTO(
        String key,
        String value
){}
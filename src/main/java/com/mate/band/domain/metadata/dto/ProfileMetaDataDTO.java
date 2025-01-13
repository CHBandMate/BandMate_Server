package com.mate.band.domain.metadata.dto;

import lombok.Builder;

@Builder
public record ProfileMetaDataDTO(
        String key,
        String value
){}
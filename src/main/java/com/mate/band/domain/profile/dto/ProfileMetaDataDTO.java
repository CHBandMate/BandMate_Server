package com.mate.band.domain.profile.dto;

import lombok.Builder;

@Builder
public record ProfileMetaDataDTO(
        String key,
        String value
){}
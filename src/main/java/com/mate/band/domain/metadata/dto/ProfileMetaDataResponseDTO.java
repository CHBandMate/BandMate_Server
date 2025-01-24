package com.mate.band.domain.metadata.dto;

import lombok.Builder;

import java.util.List;

/**
 * 프로필 메타데이터 정보 ResponseDTO
 * @author : 최성민
 * @since : 2025-01-06
 * @version : 1.0
 */
@Builder
public record ProfileMetaDataResponseDTO(
        List<ProfileMetaDataDTO> musicGenre,
        List<ProfileMetaDataDTO> position,
        List<ProfileMetaDataDTO> snsPlatform
){}

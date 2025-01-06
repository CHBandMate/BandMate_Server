package com.mate.band.domain.profile.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfileMetaDataResponseDTO(
        List<CommonCodeDTO> musicGenre,
        List<CommonCodeDTO> position,
        List<CommonCodeDTO> snsPlatform
) {

}

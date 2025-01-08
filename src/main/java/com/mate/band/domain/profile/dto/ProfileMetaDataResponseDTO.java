package com.mate.band.domain.profile.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProfileMetaDataResponseDTO(
        List<ProfileMetaDataDTO> musicGenre,
        List<ProfileMetaDataDTO> position,
        List<ProfileMetaDataDTO> snsPlatform
) {

}

package com.mate.band.domain.user.dto;

import com.mate.band.domain.metadata.dto.DistrictDataDTO;
import com.mate.band.domain.metadata.dto.ProfileMetaDataDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record UserProfileResponseDTO(
        long userId,
        String profileImageUrl,
        String nickname,
        String introduction,
        List<ProfileMetaDataDTO> genres,
        List<ProfileMetaDataDTO> positions,
        List<DistrictDataDTO> districts
) {
}

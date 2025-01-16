package com.mate.band.domain.band.dto;

import com.mate.band.domain.metadata.dto.DistrictDataDTO;
import com.mate.band.domain.metadata.dto.ProfileMetaDataDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record BandProfileResponseDTO(
        long bandId,
        long leaderId,
        String profileImageUrl,
        String leaderNickname,
        String bandName,
        String recruitTitle,
        List<ProfileMetaDataDTO> genres,
        List<ProfileMetaDataDTO> positions,
        List<DistrictDataDTO> districts,
        List<BandMemberDTO> members,
        String description,
        boolean favoriteYn
) {
}

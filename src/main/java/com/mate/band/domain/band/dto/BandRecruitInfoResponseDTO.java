package com.mate.band.domain.band.dto;

import com.mate.band.domain.metadata.dto.DistrictDataDTO;
import com.mate.band.domain.metadata.dto.ProfileMetaDataDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record BandRecruitInfoResponseDTO(
        @Schema(description = "밴드 id", example = "3")
        long bandId,

//        String bandProfileImageUrl,

        @Schema(description = "밴드명", example = "베짱이와 쩌리들")
        String bandName,

        @Schema(description = "모집글 제목", example = "베짱이와 쩌리들에서 멤버를 모집합니다~!")
        String recruitTitle,

        @Schema(description = "모집글 내용", example = "놀기만 하고 기타치는 개미와 먹고 노래만 부르는 개똥벌레 모집합니다.")
        String description,

        List<ProfileMetaDataDTO> genres,

        List<ProfileMetaDataDTO> positions,

        List<DistrictDataDTO> districts
) {}

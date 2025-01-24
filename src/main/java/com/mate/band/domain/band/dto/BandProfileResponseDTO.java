package com.mate.band.domain.band.dto;

import com.mate.band.domain.metadata.dto.DistrictDataDTO;
import com.mate.band.domain.metadata.dto.ProfileMetaDataDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * 밴드 상세 프로필 ResponseDTO
 * @author : 최성민
 * @since : 2025-01-16
 * @version : 1.0
 * TODO 소개글 추가
 */
@Builder
public record BandProfileResponseDTO(
        @Schema(description = "밴드 id", example = "3")
        long bandId,

        @Schema(description = "밴드 리더 id", example = "1")
        long leaderId,

//        String profileImageUrl,
        @Schema(description = "밴드 리더 닉네임", example = "베짱이")
        String leaderNickname,

        @Schema(description = "밴드명", example = "베짱이와 쩌리들")
        String bandName,

        @Schema(description = "모집글 제목", example = "베짱이와 쩌리들에서 멤버를 모집합니다~!")
        String recruitTitle,

        List<ProfileMetaDataDTO> genres,

        List<ProfileMetaDataDTO> positions,

        List<DistrictDataDTO> districts,

        List<BandMemberDTO> members,

        @Schema(description = "모집글 내용", example = "놀기만 하고 기타치는 개미와 먹고 노래만 부르는 개똥벌레 모집합니다.")
        String description,

        @Schema(description = "즐겨찾기 여부", example = "false")
        boolean favoriteYn
) {
}

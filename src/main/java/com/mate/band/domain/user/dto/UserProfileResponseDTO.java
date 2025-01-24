package com.mate.band.domain.user.dto;

import com.mate.band.domain.metadata.dto.DistrictDataDTO;
import com.mate.band.domain.metadata.dto.ProfileMetaDataDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * 회원 프로필 ResponseDTO
 * @author : 최성민
 * @since : 2025-01-15
 * @version : 1.0
 */
@Builder
public record UserProfileResponseDTO(
        @Schema(description = "회원Id", example = "3")
        long userId,

        @Schema(description = "프로필 이미지 url")
        String profileImageUrl,

        @Schema(description = "닉네임", example = "베짱이")
        String nickname,

        @Schema(description = "소개", example = "안녕하세요. 저는 베이스 치는 베짱이 입니다.")
        String introduction,

        List<ProfileMetaDataDTO> genres,

        List<ProfileMetaDataDTO> positions,

        List<DistrictDataDTO> districts,

        List<UserProfileBandInfoDTO> bandInfos,

        @Schema(description = "즐겨찾기 여부", example = "false")
        boolean favoriteYn
) {
}

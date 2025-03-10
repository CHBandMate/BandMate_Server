package com.mate.band.domain.band.dto;


import com.mate.band.domain.user.dto.ProfileSNSUrlDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 밴드 프로필 등록 RequestDTO
 * @author : 최성민
 * @since : 2025-01-09
 * @version : 1.0
 */
public record RegisterBandProfileRequestDTO(
        @Schema(description = "밴드 id(밴드 프로필 수정에만 필요)", example = "3")
        long bandId,

        @Schema(description = "밴드명", example = "베짱이와 쩌리들")
        String bandName,

        @Schema(description = "소개", example = "안녕하세요. 우리 밴드는 놀기만 하고 합주는 안합니다.")
        String introduction,

        List<RegisterBandMemberDTO> bandMember,

        List<ProfileSNSUrlDTO> snsUrls,

        @Schema(description = "합주 가능 지역 코드", example = "[1, 2, 3]")
        List<Long> district,

        @Schema(description = "선호 장르 코드", example = "[\"KPOP\", \"INDIE\", \"JAZZ\"]")
        List<String> genre,

        @Schema(description = "카카오 오픈 채팅 URL", example = "??")
        String kakaoOpenChatUrl,

        @Schema(description = "밴드 프로필 노출 여부", example = "true")
        boolean exposeYn,

        @Schema(description = "멤버 모집 여부", example = "true")
        boolean recruitYn,

        @Schema(description = "모집글 제목", example = "베짱이와 쩌리들에서 멤버를 모집합니다~!")
        String recruitTitle,

        @Schema(description = "모집 포지션 선택 코드", example = "[\"KEYBOARD\", \"VOCAL\", \"BASS\"]")
        List<String> recruitPosition,

        @Schema(description = "모집글 내용", example = "놀기만 하고 기타치는 개미와 먹고 노래만 부르는 개똥벌레 모집합니다.")
        String recruitDescription
        ) {}

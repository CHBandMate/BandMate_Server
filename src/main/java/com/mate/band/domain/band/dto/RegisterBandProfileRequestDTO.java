package com.mate.band.domain.band.dto;


import com.mate.band.domain.user.dto.ProfileSNSUrlDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RegisterBandProfileRequestDTO(
        @Schema(description = "밴드명", example = "베짱이와 쩌리들")
        String bandName,

        @Schema(description = "소개", example = "안녕하세요. 우리 밴드는 놀기만 하고 합주는 안합니다.")
        String introduction,

        @Schema(description = "밴드 소속 회원 번호", example = "[1, 2, 3, 4]")
        List<Long> members,

        List<ProfileSNSUrlDTO> snsUrls,

        @Schema(description = "합주 가능 지역 코드", example = "[1, 2, 3]")
        List<Long> district,

        @Schema(description = "선호 장르 코드", example = "[\"KPOP\", \"INDIE\", \"JAZZ\"]")
        List<String> genre,

        @Schema(description = "카카오 오픈 채팅 URL", example = "??")
        String kakaoOpenChatUrl,

        @Schema(description = "밴드 프로필 노출 여부", example = "true")
        boolean exposeYn,

        @Schema(description = "멤버 구인 여부", example = "true")
        boolean recruitYn,

        @Schema(description = "구인글 제목", example = "true")
        String recruitTitle,

        @Schema(description = "구인 포지션 선택 코드", example = "[\"KEYBOARD\", \"VOCAL\", \"BASS\"]")
        List<String> recruitPosition,

        @Schema(description = "구인글 내용", example = "놀기만 하고 기타치는 개미와 먹고 노래만 부르는 개똥벌레 모집합니다.")
        String recruitDescription
        ) {}

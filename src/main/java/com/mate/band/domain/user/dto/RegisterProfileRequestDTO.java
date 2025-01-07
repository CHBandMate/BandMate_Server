package com.mate.band.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
public record RegisterProfileRequestDTO(

        @NotNull
        @Schema(description = "닉네임", example = "베짱이")
        String nickName,

//        String profileImage,

        @NotNull
        @Schema(description = "카카오ID", example = "zxcv1234")
        String kakaoId,

        @Schema(description = "포지션 선택 코드", example = "[\"keyboard\", \"vocal\", \"bass\"]")
        List<String> position,

        @Schema(description = "보유 악기", example = "[\"fender jazz bass\", \"gibson lespaul\"]")
        List<String> instruments,

        @Schema(description = "보유 이펙터", example = "[\"Boss comp\", \"Boss bd1\"]")
        List<String> effectors,

        @Schema(description = "이메일", example = "zxcv1234@naver.com")
        String email,

        @Schema(description = "소개", example = "안녕하세요. 저는 베이스 치는 베짱이 입니다.")
        String introduction,

        List<ProfileSNSUrlDTO> snsUrls,

        @Schema(description = "합주 가능 지역 코드", example = "[1, 2, 3]")
        List<Long> district,

        @Schema(description = "선호 장르 코드", example = "[\"kpop\", \"indie\", \"jazz\"]")
        List<String> genre,

        @Schema(description = "프로필 노출 여부", example = "true")
        boolean exposeYn
) {
}

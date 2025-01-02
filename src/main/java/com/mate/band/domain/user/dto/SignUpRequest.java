package com.mate.band.domain.user.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SignUpRequest(
        String nickName,
        byte[] profileImage,
        String kakaoId,
        List<String> position,
        String introduction,
        boolean joinYn,
        List<String> joinPosition
) {
}

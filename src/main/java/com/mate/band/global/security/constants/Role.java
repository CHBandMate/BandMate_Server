package com.mate.band.global.security.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"),
    LEADER("ROLE_LEADER", "리더 회원"),
    USER("ROLE_USER", "일반 회원"),
    NOT_REGISTERED("ROLE_NOT_REGISTERED", "회원가입 이전 사용자");
    private final String key;
    private final String title;
}

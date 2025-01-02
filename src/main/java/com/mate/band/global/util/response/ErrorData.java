package com.mate.band.global.util.response;

import lombok.Builder;
import lombok.Getter;

/**
 * [API Response]
 * API 응답 실패에 대한 공통 포맷 정의
 * @author 최성민
 * @since 2024-12-03
 * @version 1.0
 */
@Getter
public class ErrorData {
    private String errorCode;
    private String errorMessage;

    @Builder
    public ErrorData(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
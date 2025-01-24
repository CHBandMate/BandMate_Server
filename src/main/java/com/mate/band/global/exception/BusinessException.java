package com.mate.band.global.exception;

import lombok.Getter;

/**
 * 비즈니스 로직단에서 발생 하는 Exception 처리
 * @author : 최성민
 * @since : 2025-12-22
 * @version : 1.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}

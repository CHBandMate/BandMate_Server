package com.mate.band.global.util.exception;

public class BusinessException extends RuntimeException {

    public BusinessException() {
        super();
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
    }

    public BusinessException(String errorMessage) {
        super(errorMessage);
    }

}

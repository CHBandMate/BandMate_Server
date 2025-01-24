package com.mate.band.global.exception;

/**
 * Token 만료 Exception
 * @author : 최성민
 * @since : 2025-01-03
 * @version : 1.0
 */
public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String message) {
        super(message);
    }
}

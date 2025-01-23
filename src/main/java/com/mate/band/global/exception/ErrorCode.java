package com.mate.band.global.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;

// TODO ErrorCode 정리 필요
/**
 * [API Response]
 * Error 관련 코드 정의
 * @author 최성민
 * @since 2024-12-03
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    //************************************************ Client Error ************************************************//
    NOT_FOUND(404, "NOT_FOUND", "Not Found Exception"),
    NULL_POINT(500, "NULL_POINT", "Null Point Exception"),
    BAD_REQUEST(400, "BAD_REQUEST", "Bad Request Exception"),
    BAD_REQUEST_METHOD(405, "BAD_REQUEST_METHOD", "Bad Request HTTP Method"),
    BAD_REQUEST_HEADER(400, "BAD_REQUEST_HEADER", "Request header is insufficient"),
    BAD_REQUEST_BODY(400, "BAD_REQUEST_BODY", "Request body is insufficient"),
    BAD_REQUEST_PARAMETER(400, "BAD_REQUEST_PARAMETER", "No request parameter"),
    ILLEGAL_ARGUMENT(400, "ILLEGAL_ARGUMENT", "Illegal or inappropriate argument"),
    JSON_PARSE(400, "JSON_PARSE", "Json parsing Exception"),
    NOT_EXIST_CODE(400, "NOT_EXIST_CODE", "존재하지 않는 코드입니다."),
    NOT_EXIST_USER(400, "NOT_EXIST_USER", "존재하지 않는 회원입니다."),
    NOT_EXIST_BAND(400, "NOT_EXIST_BAND", "존재하지 않는 밴드입니다."),
    REGISTERED_USER(400, "REGISTERED_USER", "이미 등록 된 회원입니다."),
    ALREADY_APPLIED_BAND(400, "ALREADY_APPLIED_BAND", "이미 지원 한 밴드입니다."),
    ALREADY_BAND_MEMBER(400, "ALREADY_BAND_MEMBER", "이미 가입 되어있는 밴드입니다."),
    ALREADY_INVITED_USER(400, "ALREADY_INVITED_USER", "이미 초대한 회원입니다."),
    ACCESS_DENIED_DATA(400, "ACCESS_DENIED_DATA", "접근이 불가능한 요청입니다."),
    OTHER_ERROR(400, "OTHER_ERROR", "Other Error"),

    //************************************************ Auth Error ************************************************//
    UNAUTHORIZED(401, "UNAUTHORIZED", "인증 되지 않은 사용자입니다."),
    TOKEN_NOT_ALLOWED(401, "TOKEN_NOT_ALLOWED", "허용 되지 않은 토큰입니다."),
    TOKEN_EXPIRED(409, "TOKEN_EXPIRED", "토큰이 만료 되었습니다."),
    TOKEN_NUll(401, "TOKEN_NUll", "토큰이 존재하지 않습니다."),
    NOT_ACCESS_TOKEN(401, "NOT_ACCESS_TOKEN", "It's not AccessToken"),
    NOT_REFRESH_TOKEN(401, "NOT_REFRESH_TOKEN", "It's not RefreshToken"),
    NOT_EXIST_AUTH_TEMP_TOKEN(401, "NOT_EXIST_AUTH_TEMP_TOKEN", "존재하지 않는 임시 코드입니다."),
    NOT_EXIST_REFRESH_TOKEN(401, "NOT_EXIST_REFRESH_TOKEN", "존재하지 않는 Refresh 토큰입니다."),
    TOKEN_PARSING(401, "TOKEN_PARSING", "Token Parsing Error"),
    ACCESS_DENIED(403, "ACCESS_DENIED", "접근권한이 없습니다."),
    OTHER_TOKEN_ERROR(401, "OTHER_TOKEN_ERROR", "알수 없는 토큰 에러가 발생했습니다."),

    //************************************************ Server Error ************************************************//
    INTERNAL_SERVER(500, "INTERNAL_SERVER", "Internal Server Error"),                     // 서버 내부 에러
    ;

    private final int statusCode;         // 에러 상태 코드
    private final String errorCode;       // 에러 코드
    private final String errorMessage;    // 에러 메시지
}

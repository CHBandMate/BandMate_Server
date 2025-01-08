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
    NOT_FOUND(404, "NOT_FOUND", "Not Found Exception"),                        // 서버로 요청한 리소스가 존재하지 않음
    NULL_POINT(500, "NULL_POINT", "Null Point Exception"),                      // NULL Point Exception 발생
    BAD_REQUEST(400, "BAD_REQUEST", "Bad Request Exception"),                    // 잘못된 서버 요청
    BAD_REQUEST_METHOD(405, "BAD_REQUEST_METHOD", "Bad Request HTTP Method"),           // 잘못된 HTTP Request Method 요청
    BAD_REQUEST_HEADER(400, "BAD_REQUEST_HEADER", "Request header is insufficient"),    // Request Header 정보가 잘못된 경우
    BAD_REQUEST_BODY(400, "BAD_REQUEST_BODY", "Request body is insufficient"),        // Request Body가 정보가 잘못된 경우
    BAD_REQUEST_PARAMETER(400, "BAD_REQUEST_PARAMETER", "No request parameter"),           // 파라미터가 존재하지 않는 경우
    ILLEGAL_ARGUMENT(400, "ILLEGAL_ARGUMENT", "Illegal or inappropriate argument"),   // 메소드에 잘못된 인자가 넘어가는 경우
    NOT_EXIST_CODE(400, "NOT_EXIST_CODE", "존재하지 않는 코드 입니다."),   // 메소드에 잘못된 인자가 넘어가는 경우
    JSON_PARSE(400, "JSON_PARSE", "Json parsing Exception"),                    // Json 파싱 에러가 발생하는 경우
    REGISTERED_USER(400, "ALREADY_EXIST_USER", "이미 등록 된 회원입니다."),                    // Json 파싱 에러가 발생하는 경우
    OTHER_ERROR(400, "OTHER_ERROR", "Other Error"),                              // 그 외 모든 에러

    //************************************************ Auth Error ************************************************//
    UNAUTHORIZED(401, "UNAUTHORIZED", "인증 되지 않은 사용자 입니다."),
    TOKEN_NOT_ALLOWED(401, "TOKEN_NOT_ALLOWED", "허용 되지 않은 토큰 입니다."),
    TOKEN_EXPIRED(409, "TOKEN_EXPIRED", "토큰이 만료 되었습니다."),
    TOKEN_NUll(401, "TOKEN_NUll", "토큰이 존재하지 않습니다."),
    NOT_ACCESS_TOKEN(401, "NOT_ACCESS_TOKEN", "It's not AccessToken"),
    NOT_REFRESH_TOKEN(401, "NOT_REFRESH_TOKEN", "It's not RefreshToken"),
    NOT_EXIST_AUTH_TEMP_TOKEN(401, "NOT_EXIST_AUTH_TEMP_TOKEN", "존재하지 않는 임시 코드 입니다."),
    NOT_EXIST_REFRESH_TOKEN(401, "NOT_EXIST_REFRESH_TOKEN", "존재하지 않는 Refresh 토큰 입니다."),
    TOKEN_PARSING(401, "TOKEN_PARSING", "Token Parsing Error"),
    ACCESS_DENIED(403, "ACCESS_DENIED", "접근권한이 없습니다."),
    OTHER_TOKEN_ERROR(401, "OTHER_TOKEN_ERROR", "알수 없는 토큰 에러가 발생 했습니다."),

    //************************************************ Server Error ************************************************//
    INTERNAL_SERVER(500, "INTERNAL_SERVER", "Internal Server Error"),                     // 서버 내부 에러
    ;

    private final int statusCode;         // 에러 상태 코드
    private final String errorCode;       // 에러 코드
    private final String errorMessage;    // 에러 메시지
}

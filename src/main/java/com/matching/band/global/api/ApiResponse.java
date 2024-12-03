package com.matching.band.global.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * [API Response]
 * API 응답 공통 포맷 정의
 * @author 최성민
 * @since 2024-12-03
 * @version 1.0
 */
@Getter
public class ApiResponse<T> {
    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final int SUCCESS_STATUS_CODE = 200;
    private static final int FAIL_STATUS_CODE = 400;
    private String status;	// 응답 성공, 실패 여부
    private int statusCode;	// HttpStatus Code
    private T data;		// 응답 데이터

    private ApiResponse(String status, int statusCode, T data) {
        this.status = status;
        this.statusCode = statusCode;
        this.data = data;
    }

    /**
     * [Success Response]
     * API response data 값이 존재할 때 호출
     * @param data
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS_STATUS, SUCCESS_STATUS_CODE, data);
    }

    /**
     * [Success Response]
     * API response data 값이 존재할 때 호출
     * @param statusCode, data
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> success(int statusCode, T data) {
        return new ApiResponse<>(SUCCESS_STATUS, statusCode, data);
    }

    /**
     * [Success Response]
     * API response data 값이 존재할 때 호출
     * @param httpStatus, data
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> success(HttpStatus httpStatus, T data) {
        return new ApiResponse<>(SUCCESS_STATUS, httpStatus.value(), data);
    }

    /**
     * [Success Response]
     * API response data 값이 필요 없을 때 호출
     * @return ApiResponse<T>
     */
    public static ApiResponse<?> success() {
        return new ApiResponse<>(SUCCESS_STATUS, SUCCESS_STATUS_CODE, null);
    }

    /**
     * [Fail Response]
     * API 응답에 실패 했을 때 호출
     * @param statusCode, errorData
     * @return ApiResponse<errorData>
     */
    public static ApiResponse<ErrorData> fail(int statusCode, ErrorData errorData) {
        return new ApiResponse<>(FAIL_STATUS, statusCode, errorData);
    }


    /**
     * [Fail Response]
     * API 응답에 실패 했을 때 호출
     * @param httpStatus, errorData
     * @return ApiResponse<errorData>
     */
    public static ApiResponse<ErrorData> fail(HttpStatus httpStatus, ErrorData errorData) {
        return new ApiResponse<>(FAIL_STATUS, httpStatus.value(), errorData);
    }


}

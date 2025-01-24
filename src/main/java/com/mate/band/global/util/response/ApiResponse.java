package com.mate.band.global.util.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
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
     * API response data 값이 존재할 때 호출
     * @param data Response 데이터
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS_STATUS, SUCCESS_STATUS_CODE, data);
    }

    /**
     * API response data 값이 존재할 때 호출
     * @param statusCode 상태 코드
     * @param data Response 데이터
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> success(int statusCode, T data) {
        return new ApiResponse<>(SUCCESS_STATUS, statusCode, data);
    }

    /**
     * API response data 값이 존재할 때 호출
     * @param httpStatus HttpStatus
     * @param data Response 데이터
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> success(HttpStatus httpStatus, T data) {
        return new ApiResponse<>(SUCCESS_STATUS, httpStatus.value(), data);
    }

    /**
     * API response data 값이 필요 없을 때 호출
     * @return ApiResponse<T>
     */
    public static ApiResponse<?> success() {
        return new ApiResponse<>(SUCCESS_STATUS, SUCCESS_STATUS_CODE, null);
    }

    /**
     * API 응답에 실패 했을 때 호출
     * @param statusCode 상태 코드
     * @param errorData Response 에러 데이터
     * @return ApiResponse errorData
     */
    public static ApiResponse<ErrorData> fail(int statusCode, ErrorData errorData) {
        return new ApiResponse<>(FAIL_STATUS, statusCode, errorData);
    }


    /**
     * API 응답에 실패 했을 때 호출
     * @param httpStatus HttpStatus
     * @param errorData Response 에러 데이터
     * @return ApiResponse errorData
     */
    public static ApiResponse<ErrorData> fail(HttpStatus httpStatus, ErrorData errorData) {
        return new ApiResponse<>(FAIL_STATUS, httpStatus.value(), errorData);
    }


}

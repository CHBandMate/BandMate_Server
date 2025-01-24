package com.mate.band.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.band.global.exception.ErrorCode;
import com.mate.band.global.util.response.ApiResponse;
import com.mate.band.global.util.response.ErrorData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 접근 권한 부족 Exception 관리
 * @author : 최성민
 * @since : 2024-12-30
 * @version : 1.0
 * TODO 어떤 계정이 deny 됐는지 로깅 필요
 */
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn(accessDeniedException.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        ApiResponse<ErrorData> errorData = ApiResponse.fail(ErrorCode.ACCESS_DENIED.getStatusCode(), new ErrorData(ErrorCode.ACCESS_DENIED));
        String responseBody = objectMapper.writeValueAsString(errorData);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(ErrorCode.ACCESS_DENIED.getStatusCode());
        response.getWriter().print(responseBody);
    }
}

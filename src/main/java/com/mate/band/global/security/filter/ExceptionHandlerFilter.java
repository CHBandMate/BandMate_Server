package com.mate.band.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.band.global.exception.ErrorCode;
import com.mate.band.global.exception.TokenExpiredException;
import com.mate.band.global.exception.TokenNullException;
import com.mate.band.global.util.response.ApiResponse;
import com.mate.band.global.util.response.ErrorData;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 인가 관련 Exception 관리
 * @author 최성민
 * @since 2024-12-31
 */
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            setErrorResponse(request, response, exception);
        }
    }

    private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorCode errorCode = mapErrorCode(request, exception);
        ApiResponse<ErrorData> errorData = ApiResponse.fail(errorCode.getStatusCode(), new ErrorData(errorCode));
        String responseBody = objectMapper.writeValueAsString(errorData);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(errorCode.getStatusCode());
        response.getWriter().print(responseBody);
    }

    private ErrorCode mapErrorCode(HttpServletRequest request, Exception exception) {
        if (exception instanceof TokenExpiredException) {
            log.info(exception.getLocalizedMessage());
            return ErrorCode.TOKEN_EXPIRED;
        } else if (exception instanceof TokenNullException) {
            log.warn("{} Request URI: {}", exception.getLocalizedMessage(), request.getRequestURI());
            return ErrorCode.TOKEN_NUll;
        } else if (exception instanceof JwtException) {
            log.warn(exception.getLocalizedMessage(), exception);
            return ErrorCode.UNAUTHORIZED;
        } else {
            log.error(exception.getLocalizedMessage(), exception);
            return ErrorCode.OTHER_ERROR;
        }
    }
}


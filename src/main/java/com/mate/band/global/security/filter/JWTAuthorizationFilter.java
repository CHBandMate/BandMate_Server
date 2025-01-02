package com.mate.band.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.band.global.config.RedisService;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.exception.ErrorCode;
import com.mate.band.global.security.constants.Auth;
import com.mate.band.global.security.constants.Claim;
import com.mate.band.global.security.constants.TokenStatus;
import com.mate.band.global.security.service.AuthService;
import com.mate.band.global.security.service.JWTUtils;
import com.mate.band.global.util.response.ApiResponse;
import com.mate.band.global.util.response.ErrorData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static com.mate.band.global.security.config.WebSecurityConfig.PERMITTED_URI;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isPermittedURI(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // AccessToken 검증
        String accessToken = JWTUtils.getTokenFromHeader(request.getHeader(Auth.ACCESS_HEADER.getValue()));
        if (accessToken == null || accessToken.equalsIgnoreCase("")) {
            throw new NullPointerException(ErrorCode.TOKEN_NUll.getErrorMessage());
        }

        TokenStatus tokenStatus = JWTUtils.getTokenStatus(Auth.ACCESS_TYPE, accessToken);
        if (tokenStatus == TokenStatus.AUTHENTICATED) {
            // Refresh 토큰에 의한 접근 방지
            String tokenType = JWTUtils.getPrivateClaim(accessToken, Claim.TOKEN_TYPE);
            if(tokenType.equals(Auth.REFRESH_TYPE.getValue())) {
                throw new BusinessException(ErrorCode.OTHER_TOKEN_ERROR);
            }
            Authentication authentication = JWTUtils.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("토큰 검증 완료");

            filterChain.doFilter(request, response);
            return;
        }

        // 로그아웃 처리
        logout(response);
    }

    private boolean isPermittedURI(String requestURI) {
        return Arrays.stream(PERMITTED_URI)
                .anyMatch(permitted -> {
                String replace = permitted.replace("*", "");
                return requestURI.contains(replace) || replace.contains(requestURI);
            });
    }

    private void logout(HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject responseBody =
                new JSONObject(objectMapper.convertValue(ApiResponse.fail(HttpStatus.UNAUTHORIZED, new ErrorData(ErrorCode.TOKEN_EXPIRED)), Map.class));
        response.setStatus(ErrorCode.TOKEN_EXPIRED.getStatusCode());
        response.setContentType("application/json");
        response.getWriter().print(responseBody);
    }

}

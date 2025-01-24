package com.mate.band.global.security.filter;

import com.mate.band.global.exception.ErrorCode;
import com.mate.band.global.exception.TokenExpiredException;
import com.mate.band.global.exception.TokenNullException;
import com.mate.band.global.security.constants.Auth;
import com.mate.band.global.security.constants.Claim;
import com.mate.band.global.security.constants.TokenStatus;
import com.mate.band.global.security.service.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.mate.band.global.security.config.WebSecurityConfig.PERMITTED_URI;

/**
 * 토큰 검증 관련 Filter
 * @author : 최성민
 * @since : 2024-12-30
 * @version : 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String headerToken = request.getHeader(Auth.ACCESS_HEADER.getValue());
        if (isPermittedURI(request) && headerToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // AccessToken 검증
        String accessToken = JWTUtils.getTokenFromHeader(headerToken);
        if (accessToken == null || accessToken.equalsIgnoreCase("")) {
            throw new TokenNullException(ErrorCode.TOKEN_NUll.getErrorMessage());
        }

        TokenStatus tokenStatus = JWTUtils.getTokenStatus(Auth.ACCESS_TYPE, accessToken);
        if (tokenStatus != TokenStatus.AUTHENTICATED) {
            throw new TokenExpiredException(ErrorCode.TOKEN_EXPIRED.getErrorMessage());
        }

        // Refresh 토큰에 의한 접근 방지
        String tokenType = JWTUtils.getPrivateClaim(accessToken, Claim.TOKEN_TYPE);
        if (tokenType.equals(Auth.REFRESH_TYPE.getValue())) {
            throw new JwtException(ErrorCode.OTHER_TOKEN_ERROR.getErrorMessage());
        }

        setAuthentication(accessToken);
        filterChain.doFilter(request, response);
    }

    private boolean isPermittedURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        if ((requestURI.equals("/band/profile") || requestURI.equals("/user/profile"))
                && (method.equals("POST") || method.equals("PUT"))) {
            return false;
        }

        return Arrays.stream(PERMITTED_URI)
                .anyMatch(permitted -> {
                String replace = permitted.replace("*", "");
                return requestURI.contains(replace) || replace.contains(requestURI);
            });
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = JWTUtils.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}

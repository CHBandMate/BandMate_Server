package com.mate.band.global.security.filter;

import com.mate.band.global.security.constants.Auth;
import com.mate.band.global.security.constants.Claim;
import com.mate.band.global.security.service.JWTUtils;
import com.mate.band.global.util.exception.BusinessException;
import com.mate.band.global.util.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.mate.band.global.security.config.WebSecurityConfig.PERMITTED_URI;

@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isPermittedURI(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // AccessToken 검증
        String headerAccessToken = request.getHeader(Auth.ACCESS_HEADER.getKey());
        if (headerAccessToken == null || headerAccessToken.equalsIgnoreCase("")) {
            throw new NullPointerException(ErrorCode.TOKEN_NUll.getErrorMessage());
        }

        if (JWTUtils.isValidToken(headerAccessToken)) {
            // Refresh 토큰에 의한 접근 방지
            String tokenType = JWTUtils.getPrivateClaim(headerAccessToken, Claim.TOKEN_TYPE);
            if(!request.getRequestURI().equals("/auth/reissue") && tokenType.equals(Auth.REFRESH_TYPE.getKey())) {
                throw new BusinessException(ErrorCode.OTHER_TOKEN_ERROR);
            }
            Authentication authentication = JWTUtils.getAuthentication(headerAccessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }

    private boolean isPermittedURI(String requestURI) {
        return Arrays.stream(PERMITTED_URI)
                .anyMatch(permitted -> {
                String replace = permitted.replace("*", "");
                return requestURI.contains(replace) || replace.contains(requestURI);
            });
    }

}

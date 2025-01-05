package com.mate.band.global.security.controller;

import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.global.security.annotation.AuthUser;
import com.mate.band.global.security.constants.Auth;
import com.mate.band.global.security.dto.TokenRequest;
import com.mate.band.global.security.service.AuthService;
import com.mate.band.global.util.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AuthController", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "토큰 발급", description = "소셜 로그인 후 임시토큰과 식별자를 통해 AccessToken, RefreshToken 발급")
    @PostMapping("/token")
    public ApiResponse<?> issueToken(HttpServletResponse response, @RequestBody TokenRequest tokenRequest) {
        authService.issueToken(response, tokenRequest);
        return ApiResponse.success();
    }

    @Operation(summary = "토큰 재발급",
            description = "AccessToken 만료 시 RefreshToken으로 재발급 신청" +
                    "</br> RefreshToken도 만료 시 로그아웃 처리")
    @GetMapping("/token/reissue")
    public ApiResponse<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(Auth.REFRESH_HEADER.getValue());
        authService.reissueToken(response, refreshToken);
        return ApiResponse.success();
    }

    @Operation(hidden = true)
    @GetMapping("/logout")
    public void logout(@AuthUser UserEntity user) {
        System.out.println(user.getUserNo());
        System.out.println(user.getRole().getKey());
    }
}

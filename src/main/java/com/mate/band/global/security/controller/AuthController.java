package com.mate.band.global.security.controller;

import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.global.security.annotation.AuthUser;
import com.mate.band.global.security.constants.Auth;
import com.mate.band.global.security.domain.UserPrincipal;
import com.mate.band.global.security.dto.TokenRequestDTO;
import com.mate.band.global.security.dto.TokenResponseDTO;
import com.mate.band.global.security.service.AuthService;
import com.mate.band.global.util.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 Controller
 * @author : 최성민
 * @since : 2024-01-02
 * @version : 1.0
 */
@Tag(name = "AuthController", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "토큰 발급",
            description = "소셜 로그인 후 임시토큰과 식별자를 통해 AccessToken, RefreshToken 발급합니다." +
                    "</br> <b>임시 토큰은 1회용 입니다." +
                    "</br> JWT 발급에 실패 하더라도 보안을 위해 임시토큰은 DB에서 제거되기 때문에 로그인을 통해 다시 발급 받아야 합니다.</b>")
    @PostMapping("/token")
    public ApiResponse<TokenResponseDTO> issueToken(HttpServletResponse response, @RequestBody TokenRequestDTO tokenRequest) {
        authService.issueToken(response, tokenRequest);
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity user = principal.getUser();
        return ApiResponse.success(new TokenResponseDTO(user.getRole().getKey()));
    }

    @Operation(summary = "토큰 재발급",
            description = "AccessToken이 만료 됐을때 RefreshToken으로 재발급 신청합니다." +
                    "</br> RefreshToken도 만료 되면 로그아웃 처리합니다." +
                    "</br> <b>AccessToken, RefreshToken 만료 -> Http StatusCode : 409</b>")
    @GetMapping("/token/reissue")
    public ApiResponse<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(Auth.REFRESH_HEADER.getValue());
        authService.reissueToken(response, refreshToken);
        return ApiResponse.success();
    }

    @Operation(summary = "로그아웃",
            description = "DB에 저장 된 RefreshToken을 제거합니다.")
    @GetMapping("/logout")
    public void logout(@AuthUser UserEntity user) {
        authService.logout(user);
    }
}

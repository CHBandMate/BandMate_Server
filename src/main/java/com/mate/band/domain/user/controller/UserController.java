package com.mate.band.domain.user.controller;

import com.mate.band.domain.user.dto.RegisterProfileRequestDTO;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.service.UserService;
import com.mate.band.global.security.annotation.AuthUser;
import com.mate.band.global.util.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserController", description = "회원 관련 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "개인 프로필 등록")
    @PostMapping("/profile")
    public ApiResponse<?> registerProfile(@AuthUser UserEntity user, @RequestBody RegisterProfileRequestDTO registerProfileRequest) {
        userService.registerProfile(user, registerProfileRequest);
        return ApiResponse.success();
    }

}

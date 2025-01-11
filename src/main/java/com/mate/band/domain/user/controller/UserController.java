package com.mate.band.domain.user.controller;

import com.mate.band.domain.user.dto.FindUserResponseDTO;
import com.mate.band.domain.user.dto.RegisterUserProfileRequestDTO;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.service.UserService;
import com.mate.band.global.security.annotation.AuthUser;
import com.mate.band.global.util.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "UserController", description = "회원 관련 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "개인 프로필 등록")
    @PostMapping("/profile")
    public ApiResponse<?> registerProfile(@AuthUser UserEntity user, @RequestBody RegisterUserProfileRequestDTO profileRequest) {
        userService.registerUserProfile(user, profileRequest);
        return ApiResponse.success();
    }

    @Operation(summary = "닉네임 중복 확인", description = "true : 중복 O</br>false : 중복 X")
    @GetMapping("/profile/check-nickname")
    public ApiResponse<Boolean> checkNickname(@Schema(description = "닉네임", example = "최감자") @RequestParam String nickname) {
        return ApiResponse.success(userService.findUserByNickname(nickname).isPresent());
    }

    @Operation(summary = "닉네임으로 회원 검색", description = "회원이 존재하지 않을 경우 \"data\" : null")
    @GetMapping("/{nickname}")
    public ApiResponse<?> findUserByNickname(@PathVariable String nickname) {
        Optional<UserEntity> user = userService.findUserByNickname(nickname);
        if (user.isEmpty()) {
            return ApiResponse.success();
        } else {
            UserEntity userEntity = user.get();
            return ApiResponse.success(FindUserResponseDTO.builder().userId(userEntity.getId()).nickname(userEntity.getNickname()).build());
        }
    }

}

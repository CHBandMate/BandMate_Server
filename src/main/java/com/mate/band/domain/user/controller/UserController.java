package com.mate.band.domain.user.controller;

import com.mate.band.domain.user.dto.SignUpRequest;
import com.mate.band.domain.user.service.UserService;
import com.mate.band.global.util.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(hidden = true)
    @PostMapping("/signUp")
    public ApiResponse<?> signUp(@RequestBody SignUpRequest requestSignUp) {
        return ApiResponse.success();
    }

    @Operation(summary = "Swagger 테스트", description = "Swagger 테스트 API 입니다.")
    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.success("테스트 성공!");
    }

}

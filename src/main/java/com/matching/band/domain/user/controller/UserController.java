package com.matching.band.domain.user.controller;

import com.matching.band.domain.user.dto.SignUpRequest;
import com.matching.band.domain.user.service.UserService;
import com.matching.band.global.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signUp")
    public ApiResponse<?> signUp(@RequestBody SignUpRequest requestSignUp) {
        return ApiResponse.success();
    }

    @GetMapping("/test")
    public ApiResponse<String> test() {
        System.out.println("asdasdasdasdasdaddsadas");
        return ApiResponse.success("테스트 성공!");
    }

}

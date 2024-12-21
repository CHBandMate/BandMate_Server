package com.matching.band.domain.user.controller;

import com.matching.band.global.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @RequestMapping("/signUp")
    public ApiResponse<?> signUp() {
        return ApiResponse.success();
    }
}

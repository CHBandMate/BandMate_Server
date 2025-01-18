package com.mate.band.domain.user.controller;

import com.mate.band.domain.user.dto.FindUserResponseDTO;
import com.mate.band.domain.user.dto.RegisterUserProfileRequestDTO;
import com.mate.band.domain.user.dto.UserInviteRequestDTO;
import com.mate.band.domain.user.dto.UserProfileResponseDTO;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.service.UserService;
import com.mate.band.global.security.annotation.AuthUser;
import com.mate.band.global.util.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "UserController", description = "회원 관련 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    // TODO SNS 정보 저장 추가
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

    @Operation(summary = "메인 화면 회원 프로필 조회",
            description = "코드 구분자 \",\" 공백 X" +
                    "<br/> districts(지역 코드) : 1,2,3... or ALL" +
                    "<br/> genres(장르 코드) : JAZZ,KPOP... or ALL" +
                    "<br/> positions(포지션 코드) : BASS,GUITAR... or ALL" +
                    "<br/> pageNumber : 현재 페이지 / pageSize : 페이지당 게시글 수 / totalElements : 총 게시글 수 / totalPages : 전체 페이지 수")
    @GetMapping("/profile")
    public ApiResponse<Page<UserProfileResponseDTO>> getUserProfileList(
            @AuthUser UserEntity authUser,
            @Schema(description = "페이지", example = "0") @RequestParam(defaultValue = "0") int page
            , @Schema(description = "페이지 사이즈", example = "10") @RequestParam(defaultValue = "10") int size
            , @Schema(description = "검색 지역", example = "ALL") @RequestParam(defaultValue = "ALL") String districts
            , @Schema(description = "검색 장르", example = "ALL") @RequestParam(defaultValue = "ALL") String genres
            , @Schema(description = "검색 포지션", example = "ALL") @RequestParam(defaultValue = "ALL") String positions
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "userEntity.createdAt"));
        return ApiResponse.success(userService.getUserProfileList(authUser, districts, genres, positions, pageable));
    }

    @Operation(summary = "유저 프로필 조회")
    @GetMapping("/profile/{userId}")
    public ApiResponse<UserProfileResponseDTO> getUserProfile(@PathVariable long userId) {
        return ApiResponse.success(userService.getUserProfile(userId));
    }

    @Operation(summary = "유저 밴드 초대")
    @PostMapping("/invite")
    public ApiResponse<?> inviteUser(@RequestBody UserInviteRequestDTO inviteRequest) {
        userService.inviteUser(inviteRequest);
        return ApiResponse.success();
    }

}

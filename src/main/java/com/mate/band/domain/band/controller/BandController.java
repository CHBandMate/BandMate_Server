package com.mate.band.domain.band.controller;

import com.mate.band.domain.band.dto.BandRecruitInfoResponseDTO;
import com.mate.band.domain.band.dto.RegisterBandProfileRequestDTO;
import com.mate.band.domain.band.service.BandService;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.global.security.annotation.AuthUser;
import com.mate.band.global.util.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BandController", description = "밴드 관련 API")
@RestController
@RequestMapping("/band")
@RequiredArgsConstructor
public class BandController {

    private final BandService bandService;

    @Operation(summary = "밴드 프로필 등록")
    @PostMapping("/profile")
    public ApiResponse<?> registerBandProfile(@AuthUser UserEntity user, @RequestBody RegisterBandProfileRequestDTO profileRequest) {
        bandService.registerBandProfile(user, profileRequest);
        return ApiResponse.success();
    }

    @Operation(summary = "밴드명 중복 확인", description = "true : 중복 O</br>false : 중복 X")
    @GetMapping("/profile/check-band-name")
    public ApiResponse<Boolean> checkBandName(@RequestParam String bandName) {
        return ApiResponse.success(bandService.checkBandName(bandName));
    }

    // TODO 페이징 처리, FetchType 어떻게 작동 되는지 확인
    @Operation(summary = "메인 화면 멤버 모집 게시글 조회",
            description = "pageNumber : 현재 페이지 / pageSize : 페이지당 게시글 수 / totalElements : 총 게시글 수 / totalPages : 전체 페이지 수" +
                    "<br/> districts : 지역 코드, genres : 장르 코드, positions : 포지션 코드")
    @GetMapping("/main/recruit")
    public ApiResponse<Page<BandRecruitInfoResponseDTO>> getBandRecruitInfoList(
            @Schema(description = "페이지", example = "0") @RequestParam(defaultValue = "0") int page
            , @Schema(description = "페이지 사이즈", example = "10") @RequestParam(defaultValue = "10") int size
            , @Schema(description = "검색 지역", example = "1,2,3,4 or ALL(전체)") @RequestParam(defaultValue = "ALL") String districts
            , @Schema(description = "검색 장르", example = "JAZZ,JPOP or ALL(전체)") @RequestParam(defaultValue = "ALL") String genres
            , @Schema(description = "검색 포지션", example = "BASS,GUITAR or ALL(전체)") @RequestParam(defaultValue = "ALL") String positions
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bandRecruitInfoEntity.createdAt"));
        return ApiResponse.success(bandService.getBandRecruitInfoList(districts, genres, positions, pageable));
    }

}

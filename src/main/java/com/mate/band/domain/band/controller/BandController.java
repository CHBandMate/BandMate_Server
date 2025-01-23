package com.mate.band.domain.band.controller;

import com.mate.band.domain.band.dto.*;
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

import java.util.List;

/**
 * 밴드 관련 Controller
 * @author : 최성민
 * @since : 2025-01-09
 * @version : 1.0
 */
@Tag(name = "BandController", description = "밴드 관련 API")
@RestController
@RequestMapping("/band")
@RequiredArgsConstructor
public class BandController {

    private final BandService bandService;

    // TODO SNS 정보 저장 추가
    @Operation(summary = "밴드 프로필 등록",
            description = "<b>밴드 id는 프로필 수정 시에만 필요합니다.</b>")
    @PostMapping("/profile")
    public ApiResponse<?> registerBandProfile(@AuthUser UserEntity user, @RequestBody RegisterBandProfileRequestDTO profileRequest) {
        bandService.registerBandProfile(user, profileRequest);
        return ApiResponse.success();
    }

    @Operation(summary = "밴드 가입 신청")
    @PostMapping("/apply")
    public ApiResponse<?> applyBand(@AuthUser UserEntity user, @RequestBody BandApplyRequestDTO bandApplyRequest) {
        bandService.applyBand(user, bandApplyRequest);
        return ApiResponse.success();
    }

    // TODO 페이징객체 페이지 0으로 되는거 수정
    // TODO FetchType 어떻게 작동 되는지 확인
    @Operation(summary = "메인 화면 멤버 모집 게시글 조회",
            description = "<b>코드 구분자는 ',' 이고 공백은 없어야 합니다.</b>" +
                    "<br/> districts(지역 코드) : 1,2,3... or ALL" +
                    "<br/> genres(장르 코드) : JAZZ,KPOP... or ALL" +
                    "<br/> positions(포지션 코드) : BASS,GUITAR... or ALL" +
                    "<br/> recruitYn(멤버 모집 여부) : true or false" +
                    "<br/><br/> <b>페이징 정보</b>" +
                    "<br/> number : 현재 페이지 번호 / first : 첫페이지 여부 / last : 마지막 페이지 여부" +
                    "<br/> totalPages : 전체 페이지 수 / size : 페이지당 게시글 수 / totalElements : 총 게시글 수")
    @GetMapping("/profile")
    public ApiResponse<Page<BandRecruitInfoResponseDTO>> getBandRecruitInfoList(
            @Schema(description = "페이지", example = "0") @RequestParam(defaultValue = "0") int page
            , @Schema(description = "페이지 사이즈", example = "10") @RequestParam(defaultValue = "10") int size
            , @Schema(description = "검색 지역", example = "ALL") @RequestParam(defaultValue = "ALL") String districts
            , @Schema(description = "검색 장르", example = "KPOP,JAZZ") @RequestParam(defaultValue = "ALL") String genres
            , @Schema(description = "검색 포지션", example = "BASS,GUITAR,VOCAL") @RequestParam(defaultValue = "ALL") String positions
            , @Schema(description = "멤버 모집 여부", example = "true") @RequestParam(defaultValue = "true") boolean recruitYn
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "bandRecruitInfoEntity.createdAt"));
        return ApiResponse.success(bandService.getBandRecruitInfoList(districts, genres, positions, recruitYn, pageable));
    }

    @Operation(summary = "나의 밴드 프로필 조회",
            description = "내가 등록한 밴드를 조회 합니다.")
    @GetMapping("/profile/my")
    public ApiResponse<List<BandProfileResponseDTO>> getMyBandProfiles(@AuthUser UserEntity userEntity) {
        return ApiResponse.success(bandService.getMyBandProfiles(userEntity));
    }

    @Operation(summary = "밴드 프로필 조회")
    @GetMapping("/profile/{bandId}")
    public ApiResponse<BandProfileResponseDTO> getBandProfile(@PathVariable long bandId) {
        return ApiResponse.success(bandService.getBandProfileDetail(bandId));
    }

    @Operation(summary = "밴드 프로필 수정")
    @PutMapping("/profile")
    public ApiResponse<?> editBandProfile(@AuthUser UserEntity user, @RequestBody RegisterBandProfileRequestDTO profileRequest) {
        bandService.editBandProfile(user, profileRequest);
        return ApiResponse.success();
    }

    @Operation(summary = "밴드 가입 신청 현황",
            description = "내가 지원한 밴드 내역을 조회 합니다.")
    @GetMapping("/apply")
    public ApiResponse<List<BandApplyCurrentInfoResponseDTO>> getApplyCurrentInfo(@AuthUser UserEntity user) {
        return ApiResponse.success(bandService.getApplyCurrentInfo(user));
    }

    @Operation(summary = "나의 밴드 가입 지원자 현황",
            description = "나의 밴드에 지원한 유저의 내역을 조회합니다.")
    @GetMapping("/applicant/{bandId}")
    public ApiResponse<List<BandApplicantResponseDTO>> getApplicantInfo(@AuthUser UserEntity user, @PathVariable long bandId) {
        return ApiResponse.success(bandService.getApplicantInfo(user, bandId));
    }

    @Operation(summary = "밴드명 중복 확인",
            description = "<b>true : 사용 가능<br/>false : 사용 불가능</b>")
    @GetMapping("/profile/check-band-name")
    public ApiResponse<Boolean> checkBandName(@RequestParam String bandName) {
        return ApiResponse.success(bandService.checkBandName(bandName));
    }

}

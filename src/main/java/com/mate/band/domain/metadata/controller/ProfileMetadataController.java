package com.mate.band.domain.metadata.controller;

import com.mate.band.domain.metadata.dto.RegionResponseDTO;
import com.mate.band.domain.metadata.dto.ProfileMetaDataResponseDTO;
import com.mate.band.domain.metadata.service.ProfileMetadataService;
import com.mate.band.global.util.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * 프로필에 필요한 데이터 관련 Controller
 * @author : 최성민
 * @since : 2024-01-06
 * @version : 1.0
 */
@Tag(name = "ProfileMetadataController", description = "프로필 등록 메타데이터 API")
@RestController
@RequestMapping("/profile/metadata")
@RequiredArgsConstructor
public class ProfileMetadataController {

    private final ProfileMetadataService profileMetadataService;

    @Operation(summary = "음악 장르 / 밴드 포지션 / SNS 플랫폼 항목 조회")
    @GetMapping
    public ApiResponse<ProfileMetaDataResponseDTO> getProfileMetaData() {
        return ApiResponse.success(profileMetadataService.getProfileMetadata());
    }

    // TODO 캐싱 필요
    @Operation(summary = "지역 항목 조회")
    @GetMapping("/district")
    public ApiResponse<List<RegionResponseDTO>> getDistrictData() {
        return ApiResponse.success(profileMetadataService.getDistrictData());
    }

}

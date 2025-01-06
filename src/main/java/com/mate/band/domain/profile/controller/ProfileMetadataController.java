package com.mate.band.domain.profile.controller;

import com.mate.band.domain.profile.dto.RegionResponseDTO;
import com.mate.band.domain.profile.dto.ProfileMetaDataResponseDTO;
import com.mate.band.domain.profile.service.ProfileMetadataService;
import com.mate.band.global.util.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Operation(summary = "지역 항목 조회")
    @GetMapping("/region")
    public ApiResponse<List<RegionResponseDTO>> getRegionData() {
        return ApiResponse.success(profileMetadataService.getRegionData());
    }

}

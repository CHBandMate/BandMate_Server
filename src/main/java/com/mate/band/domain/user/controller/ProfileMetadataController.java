package com.mate.band.domain.user.controller;

import com.mate.band.domain.user.dto.MetaDataResponse;
import com.mate.band.domain.user.service.ProfileMetadataService;
import com.mate.band.global.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/profile")
@RequiredArgsConstructor
public class ProfileMetadataController {

    private final ProfileMetadataService profileMetadataService;

    @GetMapping("/metadata")
    public ApiResponse<MetaDataResponse> getProfileMetaData() {
        return ApiResponse.success(profileMetadataService.getProfileMetadata());
    }
}

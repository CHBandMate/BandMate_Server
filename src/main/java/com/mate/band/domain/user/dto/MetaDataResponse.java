package com.mate.band.domain.user.dto;

import com.mate.band.domain.common.dto.CommonCode;
import lombok.Builder;

import java.util.List;

@Builder
public record MetaDataResponse(
        List<CommonCode> musicGenre,
        List<CommonCode> position,
        List<CommonCode> snsPlatform
) {

}

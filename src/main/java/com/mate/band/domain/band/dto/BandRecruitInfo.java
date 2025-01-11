package com.mate.band.domain.band.dto;

import com.mate.band.domain.band.entity.BandRecruitInfoEntity;
import com.mate.band.domain.metadata.entity.DistrictMappingEntity;
import com.mate.band.domain.metadata.entity.PositionMappingEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record BandRecruitInfo(
        long bandId,
        BandRecruitInfoEntity bandRecruitInfo,
        List<PositionMappingEntity> positions,
        List<DistrictMappingEntity> districts
) {}

package com.mate.band.domain.band.repository;

import com.mate.band.domain.band.entity.BandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BandRepositoryCustom {
    Page<BandEntity> findBandList(List<Long> districts, List<String> genres, List<String> positions, boolean recruitYn, Pageable pageable);
}

package com.mate.band.domain.profile.repository;

import com.mate.band.domain.profile.dto.RegionDataDTO;
import com.mate.band.domain.profile.entity.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistrictRepository extends JpaRepository<DistrictEntity, Long> {

}

package com.mate.band.domain.profile.repository;

import com.mate.band.domain.profile.entity.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictRepository extends JpaRepository<DistrictEntity, Long> {

    @Query("select de from DistrictEntity de where de.id in :districtIdList")
    List<DistrictEntity> findByIdIn(@Param("districtIdList")  List<Long> districtIdList);
}

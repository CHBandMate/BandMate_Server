package com.mate.band.domain.common.repository;

import com.mate.band.domain.common.dto.RegionData;
import com.mate.band.domain.common.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<RegionEntity, Long> {

    @Query("select new com.mate.band.domain.common.dto.RegionData(re.id , re.regionName, de.id, de.districtName)" +
            "from RegionEntity re " +
            "inner join re.districts de " +
            "order by re.id, de.id")
    List<RegionData> getRegionsWithDistricts();

}

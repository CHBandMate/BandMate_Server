package com.mate.band.domain.metadata.repository;

import com.mate.band.domain.metadata.dto.RegionDataDTO;
import com.mate.band.domain.metadata.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionRepository extends JpaRepository<RegionEntity, Long> {

    @Query("select new com.mate.band.domain.metadata.dto.RegionDataDTO(re.id , re.regionName, de.id, de.districtName)" +
            "from RegionEntity re " +
            "inner join re.districts de " +
            "order by re.id, de.id")
    List<RegionDataDTO> getRegionsWithDistricts();

}

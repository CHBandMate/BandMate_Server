package com.mate.band.domain.band.repository;

import com.mate.band.domain.band.entity.BandRecruitInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandRecruitInfoRepository extends JpaRepository<BandRecruitInfoEntity, Long> {
}

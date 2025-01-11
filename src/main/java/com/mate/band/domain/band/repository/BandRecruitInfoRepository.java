package com.mate.band.domain.band.repository;

import com.mate.band.domain.band.entity.BandRecruitInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BandRecruitInfoRepository extends JpaRepository<BandRecruitInfoEntity, Long> {

    @Query("select new com.mate.band.domain.band.dto.BandRecruitInfo(be.bandRecruitInfoEntity, be.recruitingPositions, be.districts)" +
            "from BandEntity be " +
            "inner join be.bandRecruitInfoEntity br" +
            "inner join be.districts di " +
            "inner join be.recruitingPositions rp " +
            "order by be.bandRecruitInfoEntity.createdAt")
    List<BandRecruitInfoEntity> findAllRecruitInfo();
}

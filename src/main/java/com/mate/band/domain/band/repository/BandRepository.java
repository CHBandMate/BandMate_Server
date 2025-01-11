package com.mate.band.domain.band.repository;

import com.mate.band.domain.band.entity.BandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BandRepository extends JpaRepository<BandEntity, Long> {
    @Query("select b from BandEntity b where b.bandName = :bandName and b.deleteYn = false")
    Optional<BandEntity> findByBandName(String bandName);

    @Query("select b from BandEntity b where b.recruitYn = true and b.deleteYn = false")
    List<BandEntity> findRecuitingBandList();
}

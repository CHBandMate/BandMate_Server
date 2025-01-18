package com.mate.band.domain.band.repository;

import com.mate.band.domain.band.entity.BandApplyInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BandApplyInfoRepository extends JpaRepository<BandApplyInfoEntity, Long> {
    @Query("select bae from BandApplyInfoEntity bae where bae.user.id = :userId")
    Optional<BandApplyInfoEntity> findByUserId(long userId);
}

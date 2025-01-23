package com.mate.band.domain.band.repository;

import com.mate.band.domain.band.entity.BandApplyInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BandApplyInfoRepository extends JpaRepository<BandApplyInfoEntity, Long> {
    @Query("select bae from BandApplyInfoEntity bae where bae.band.id = :bandId and bae.user.id = :userId")
    Optional<BandApplyInfoEntity> findByBandUserId(long bandId, long userId);

    @Query("select bae from BandApplyInfoEntity bae left join fetch bae.user u where bae.band.user.id = :userId and bae.band.id = :bandId")
    List<BandApplyInfoEntity> findUserByBandId(long userId, long bandId);

    @Query("select bae from BandApplyInfoEntity bae left join fetch bae.user u where bae.user.id = :userId")
    List<BandApplyInfoEntity> findByUserId(long userId);
}

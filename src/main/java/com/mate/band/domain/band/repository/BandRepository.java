package com.mate.band.domain.band.repository;

import com.mate.band.domain.band.entity.BandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BandRepository extends JpaRepository<BandEntity, Long>, BandRepositoryCustom {
    @Query("select b from BandEntity b where b.bandName = :bandName and b.deleteYn = false")
    Optional<BandEntity> findByBandName(String bandName);

//    @Query("select b from BandEntity b " +
//            "left join fetch b.bandRecruitInfoEntity " +
//            "left join b.districts d " +
//            "left join b.musicGenres m " +
//            "left join b.recruitingPositions r " +
//            "where b.recruitYn = true " +
//            "and b.deleteYn = false " +
//            "and (:districts IS NULL OR d.district.id IN :districts) " +
//            "and (:genres IS NULL OR m.genre IN :genres) " +
//            "and (:positions IS NULL OR r.position IN :positions) ")
//    Page<BandEntity> findRecruitingBandList(@Param("districts") List<Long> districts, @Param("genres") List<String> genres, @Param("positions") List<String> positions, Pageable pageable);
}

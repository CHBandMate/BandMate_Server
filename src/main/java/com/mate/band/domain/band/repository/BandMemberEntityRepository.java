package com.mate.band.domain.band.repository;

import com.mate.band.domain.band.entity.BandMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandMemberEntityRepository extends JpaRepository<BandMemberEntity, Long> {}

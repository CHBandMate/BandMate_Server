package com.mate.band.domain.metadata.repository;

import com.mate.band.domain.metadata.entity.MusicGenreMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : sungmin
 * @fileName : UserRepository
 * @since : 2024/12/17
 */
@Repository
public interface MusicGenreMappingRepository extends JpaRepository<MusicGenreMappingEntity, Long> {

}

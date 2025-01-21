package com.mate.band.domain.metadata.repository;

import com.mate.band.domain.metadata.entity.PositionMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : sungmin
 * @fileName : UserRepository
 * @since : 2024/12/17
 */
@Repository
public interface PositionMappingRepository extends JpaRepository<PositionMappingEntity, Long> {
    @Query("select p from PositionMappingEntity p where p.type = 'USER' and p.user.id = :userId")
    List<PositionMappingEntity> findAllByUserId(long userId);
}

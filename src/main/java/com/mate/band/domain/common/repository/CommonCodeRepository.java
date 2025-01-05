package com.mate.band.domain.common.repository;

import com.mate.band.domain.common.dto.CommonCode;
import com.mate.band.domain.common.entity.CommonCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCodeEntity, Long> {

    @Query("select new com.mate.band.domain.common.dto.CommonCode(cc.codeValue, cc.codeName) from CommonCodeEntity cc where cc.codeGroup = :codeGroup")
    List<CommonCode> findValueByCodeGroup(String codeGroup);

    @Query("select cc from CommonCodeEntity cc where cc.codeValue = :codeValue")
    Optional<CommonCodeEntity> findByCodeValue(String codeValue);

    @Query("select cc from CommonCodeEntity cc where cc.codeGroup = :codeGroup and cc.codeValue = :codeValue")
    Optional<CommonCodeEntity> findByCodeInfo(@Param("codeGroup") String codeGroup, @Param("codeValue") String codeValue);
}

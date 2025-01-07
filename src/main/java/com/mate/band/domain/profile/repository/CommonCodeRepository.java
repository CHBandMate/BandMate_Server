package com.mate.band.domain.profile.repository;

import com.mate.band.domain.profile.dto.CommonCodeDTO;
import com.mate.band.domain.profile.entity.CommonCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCodeEntity, Long> {

    @Query("select new com.mate.band.domain.profile.dto.CommonCodeDTO(cc.codeValue, cc.codeName) from CommonCodeEntity cc where cc.codeGroup = :codeGroup")
    List<CommonCodeDTO> findValuesByCodeGroup(String codeGroup);

    @Query("select cc from CommonCodeEntity cc where cc.codeValue = :codeValue")
    Optional<CommonCodeEntity> findByCodeValue(String codeValue);

    @Query("select cc from CommonCodeEntity cc where cc.codeGroup = :codeGroup and cc.codeValue = :codeValue")
    CommonCodeEntity findByCodeInfo(@Param("codeGroup") String codeGroup, @Param("codeValue") String codeValue);

    @Query("select cc from CommonCodeEntity cc where cc.codeGroup = :codeGroup and cc.codeValue in :codeValueList")
    List<CommonCodeEntity> findByCodeIn(@Param("codeGroup") String codeGroup, @Param("codeValueList") List<String> codeValueList);
}

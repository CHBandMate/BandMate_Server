package com.mate.band.domain.user.repository;

import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.global.security.constants.OAuthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author : sungmin
 * @fileName : UserRepository
 * @since : 2024/12/17
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByOauthId(String identifier);

    @Query("select u from UserEntity u where u.oauthId = :oauthId")
    List<UserEntity> findAllByOauthId(String oauthId);

    Optional<UserEntity> findByNickname(String nickname);

    @Query("select u from UserEntity u where u.oauthId = :oauthId and u.oauthType = :oauthType")
    Optional<UserEntity> findByOAuthInfo(@Param("oauthId") String oauthId, @Param("oauthType") OAuthType oauthType);
}

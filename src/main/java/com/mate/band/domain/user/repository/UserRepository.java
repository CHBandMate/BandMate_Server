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
public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositoryCustom {

    @Query("select distinct u from UserEntity u left join fetch u.bands b where u.id = :userId and u.deleteYn = false")
    Optional<UserEntity> findById(long userId);

    Optional<UserEntity> findByOauthId(String oauthId);

    @Query("select u from UserEntity u where u.nickname = :nickname and u.deleteYn = false")
    Optional<UserEntity> findByNickname(String nickname);

    @Query("select u from UserEntity u where u.oauthId = :oauthId and u.oauthType = :oauthType")
    Optional<UserEntity> findByOAuthInfo(@Param("oauthId") String oauthId, @Param("oauthType") OAuthType oauthType);

    @Query("select u from UserEntity u where u.exposeYn = true and u.deleteYn = false")
    List<UserEntity> findExposeUser();

}

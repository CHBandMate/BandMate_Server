package com.mate.band.domain.user.repository;

import com.mate.band.domain.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    Page<UserEntity> findUserList(UserEntity authUser, List<Long> districts, List<String> genres, List<String> positions, Pageable pageable);
}

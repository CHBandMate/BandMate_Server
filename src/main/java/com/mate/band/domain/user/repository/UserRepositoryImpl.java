package com.mate.band.domain.user.repository;

import com.mate.band.domain.metadata.constants.MusicGenre;
import com.mate.band.domain.metadata.constants.Position;
import com.mate.band.domain.metadata.entity.*;
import com.mate.band.domain.user.entity.QUserEntity;
import com.mate.band.domain.user.entity.UserEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UserEntity> findUserList(UserEntity authUser, List<Long> districts, List<String> genres, List<String> positions, Pageable pageable) {
        QUserEntity userEntity = QUserEntity.userEntity;
        ListPath<DistrictMappingEntity, QDistrictMappingEntity> districtList = userEntity.districts;
        ListPath<MusicGenreMappingEntity, QMusicGenreMappingEntity> musicGenreList = userEntity.musicGenres;
        ListPath<PositionMappingEntity, QPositionMappingEntity> positionList = userEntity.positions;

        BooleanBuilder builder = new BooleanBuilder();

        if (authUser != null) {
            builder.and(userEntity.id.ne(authUser.getId()));
        }
        if (!districts.isEmpty()) {
            builder.and(districtList.any().district.id.in(districts));
        }
        if (!genres.isEmpty()) {
            builder.and(musicGenreList.any().genre.in(MusicGenre.values(genres)));
        }
        if (!positions.isEmpty()) {
            builder.and(positionList.any().position.in(Position.values(positions)));
        }
        builder.and(userEntity.exposeYn.eq(true))
                .and(userEntity.deleteYn.eq(false));

        List<UserEntity> content = jpaQueryFactory.
                select(userEntity)
                .from(userEntity)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = content.size() > pageable.getPageSize();
        return new PageImpl<>(
                hasNext ? content.subList(0, pageable.getPageSize()) : content,
                pageable,
                hasNext ? pageable.getOffset() + pageable.getPageSize() : pageable.getOffset() + content.size()
        );
    }


}

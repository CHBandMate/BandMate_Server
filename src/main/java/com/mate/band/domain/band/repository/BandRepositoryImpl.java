package com.mate.band.domain.band.repository;

import com.mate.band.domain.band.entity.BandEntity;
import com.mate.band.domain.band.entity.QBandEntity;
import com.mate.band.domain.band.entity.QBandRecruitInfoEntity;
import com.mate.band.domain.metadata.constants.MusicGenre;
import com.mate.band.domain.metadata.constants.Position;
import com.mate.band.domain.metadata.entity.*;
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
public class BandRepositoryImpl implements BandRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BandEntity> findRecruitingBandList(List<Long> districts, List<String> genres, List<String> positions, Pageable pageable) {
        QBandEntity bandEntity = QBandEntity.bandEntity;
        ListPath<DistrictMappingEntity, QDistrictMappingEntity> districtList = bandEntity.districts;
        ListPath<MusicGenreMappingEntity, QMusicGenreMappingEntity> musicGenreList = bandEntity.musicGenres;
        ListPath<PositionMappingEntity, QPositionMappingEntity> positionList = bandEntity.recruitingPositions;
        QBandRecruitInfoEntity bandRecruitInfo = bandEntity.bandRecruitInfoEntity;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(bandEntity.recruitYn.eq(true)).and(bandEntity.deleteYn.eq(false));
        if (!districts.isEmpty()) {
            builder.and(districtList.any().district.id.in(districts));
        }
        if (!genres.isEmpty()) {
            builder.and(musicGenreList.any().genre.in(MusicGenre.values(genres)));
        }
        if (!positions.isEmpty()) {
            builder.and(positionList.any().position.in(Position.values(positions)));
        }

        List<BandEntity> content = jpaQueryFactory.
                selectDistinct(bandEntity)
                .from(bandEntity)
                .leftJoin(bandRecruitInfo).fetchJoin()
                .leftJoin(districtList)
                .leftJoin(musicGenreList)
                .leftJoin(positionList)
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

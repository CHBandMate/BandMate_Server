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
    public Page<BandEntity> findBandList(List<Long> districts, List<String> genres, List<String> positions, boolean recruitYn, Pageable pageable) {
        QBandEntity band = QBandEntity.bandEntity;
        ListPath<DistrictMappingEntity, QDistrictMappingEntity> districtList = band.districts;
        ListPath<MusicGenreMappingEntity, QMusicGenreMappingEntity> musicGenreList = band.musicGenres;
        ListPath<PositionMappingEntity, QPositionMappingEntity> positionList = band.recruitingPositions;
        QBandRecruitInfoEntity bandRecruitInfo = band.bandRecruitInfoEntity;

        BooleanBuilder builder = new BooleanBuilder();
        if (recruitYn) {
            builder.and(band.recruitYn.eq(true));
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
        builder.and(band.exposeYn.eq(true))
                .and(band.deleteYn.eq(false));

        List<BandEntity> content = jpaQueryFactory.
                selectDistinct(band)
                .from(band)
                .leftJoin(bandRecruitInfo).fetchJoin()
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


//    public BandProfileResponseDTO findBandProfileById(long bandId) {
//        QBandEntity band = QBandEntity.bandEntity;
//        ListPath<DistrictMappingEntity, QDistrictMappingEntity> districtList = band.districts;
//        ListPath<MusicGenreMappingEntity, QMusicGenreMappingEntity> musicGenreList = band.musicGenres;
//        ListPath<PositionMappingEntity, QPositionMappingEntity> positionList = band.recruitingPositions;
//        QBandRecruitInfoEntity bandRecruitInfo = band.bandRecruitInfoEntity;
//
//        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(band.id.eq(bandId))
//                .and(band.exposeYn.eq(true))
//                .and(band.deleteYn.eq(false));
//
//        jpaQueryFactory
//                .select(Projections.constructor(BandProfileResponseDTO.class,
//                        band.id, band.user.id, band.profileImageUrl, band.user.nickname, bandRecruitInfo.title,))
//                .from(band)
//                .where(builder).fetchOne();
//
//
//    }

}

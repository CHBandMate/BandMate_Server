package com.mate.band.domain.band.service;

import com.mate.band.domain.band.dto.*;
import com.mate.band.domain.band.entity.BandApplyInfoEntity;
import com.mate.band.domain.band.entity.BandEntity;
import com.mate.band.domain.band.entity.BandMemberEntity;
import com.mate.band.domain.band.entity.BandRecruitInfoEntity;
import com.mate.band.domain.band.repository.BandApplyInfoRepository;
import com.mate.band.domain.band.repository.BandMemberEntityRepository;
import com.mate.band.domain.band.repository.BandRecruitInfoRepository;
import com.mate.band.domain.band.repository.BandRepository;
import com.mate.band.domain.metadata.constants.MappingType;
import com.mate.band.domain.metadata.constants.MusicGenre;
import com.mate.band.domain.metadata.constants.Position;
import com.mate.band.domain.metadata.dto.DistrictDataDTO;
import com.mate.band.domain.metadata.dto.ProfileMetaDataDTO;
import com.mate.band.domain.metadata.entity.DistrictEntity;
import com.mate.band.domain.metadata.entity.DistrictMappingEntity;
import com.mate.band.domain.metadata.entity.MusicGenreMappingEntity;
import com.mate.band.domain.metadata.entity.PositionMappingEntity;
import com.mate.band.domain.metadata.repository.*;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.repository.UserRepository;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.exception.ErrorCode;
import com.mate.band.global.security.constants.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BandService {

    private final BandRepository bandRepository;
    private final UserRepository userRepository;
    private final BandMemberEntityRepository bandMemberEntityRepository;
    private final BandRecruitInfoRepository bandRecruitInfoRepository;
    private final DistrictRepository districtRepository;
    private final PositionMappingRepository positionMappingRepository;
    private final MusicGenreMappingRepository musicGenreMappingRepository;
    private final DistrictMappingRepository districtMappingRepository;
    private final BandApplyInfoRepository bandApplyInfoRepository;

    @Transactional // TODO 중간에 에러 났을때 id 값은 increment 돼있는거 왜 그런지 확인 필요
    public void registerBandProfile(UserEntity user, RegisterBandProfileRequestDTO profileParam) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        MetadataEnumRepository.verifyMetadataKey(profileParam.genre(), MusicGenre.class);
        List<DistrictEntity> districts = verifyDistrict(profileParam.district());

        BandEntity bandEntity =
                BandEntity.builder()
                        .user(userEntity)
                        .bandName(profileParam.bandName())
                        .introduction(profileParam.introduction())
                        .exposeYn(profileParam.exposeYn())
                        .recruitYn(profileParam.recruitYn())
                        .build();

        List<MusicGenreMappingEntity> musicGenreMappingEntityList = profileParam.genre().stream().map(genre ->
                MusicGenreMappingEntity.builder()
                        .type(MappingType.BAND)
                        .band(bandEntity)
                        .genre(MusicGenre.valueOf(genre))
                        .build()).toList();

        List<DistrictMappingEntity> districtMappingEntityList = districts.stream().map(district ->
                DistrictMappingEntity.builder()
                        .type(MappingType.BAND)
                        .band(bandEntity)
                        .district(district)
                        .build()).toList();

        bandRepository.save(bandEntity);
        musicGenreMappingRepository.saveAll(musicGenreMappingEntityList);
        districtMappingRepository.saveAll(districtMappingEntityList);

        // 밴드 멤버 저장
        if (!profileParam.bandMember().isEmpty()) {
            List<BandMemberEntity> bandMemberEntityList = new ArrayList<>();
            for (RegisterBandMemberDTO bandMember : profileParam.bandMember()) {
                UserEntity member;
                if (bandMember.userId() == userEntity.getId()) {    // 나 자신 일때
                    member = userEntity;
                } else {
                    member = userRepository.findById(bandMember.userId()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_USER));
                }
                bandMemberEntityList.add(BandMemberEntity.builder().band(bandEntity).user(member).position(Position.valueOf(bandMember.positionCode())).build());
            }
            bandMemberEntityRepository.saveAll(bandMemberEntityList);
        }

        // 모집 정보 저장
        if (profileParam.recruitYn()) {
            // 모집 포지션
            MetadataEnumRepository.verifyMetadataKey(profileParam.recruitPosition(), Position.class);
            List<PositionMappingEntity> positionMappingEntityList = profileParam.recruitPosition().stream().map(position ->
                    PositionMappingEntity.builder()
                            .type(MappingType.BAND)
                            .band(bandEntity)
                            .position(Position.valueOf(position))
                            .build()).toList();
            positionMappingRepository.saveAll(positionMappingEntityList);

            // 모집 내용
            BandRecruitInfoEntity recruitInfoEntity =
                    BandRecruitInfoEntity.builder()
                            .band(bandEntity)
                            .title(profileParam.recruitTitle())
                            .description(profileParam.recruitDescription())
                            .build();
            bandRecruitInfoRepository.save(recruitInfoEntity);
        }

        // 등급 변경
        user.updateRole(Role.LEADER);
    }

    public Boolean checkBandName(String bandName) {
        return bandRepository.findByBandName(bandName).isPresent();
    }

    // TODO 리팩토링, 즐겨찾기 여부 추가
    @Transactional
    public Page<BandRecruitInfoResponseDTO> getBandRecruitInfoList(String districts, String genres, String positions, boolean recruitYn, Pageable pageable) {
        List<Long> districtParam = districts.equals("ALL") ? new ArrayList<>() : Arrays.stream(districts.replaceAll(" ", "").split(",")).map(Long::valueOf).toList();
        List<String> genreParam = genres.equals("ALL") ? new ArrayList<>() : Arrays.stream(genres.replaceAll(" ", "").split(",")).toList();
        List<String> positionParam = positions.equals("ALL") ? new ArrayList<>() : Arrays.stream(positions.replaceAll(" ", "").split(",")).toList();

        Page<BandEntity> recruitingBandList = bandRepository.findBandList(districtParam, genreParam, positionParam, recruitYn, pageable);
        return recruitingBandList.map(recruitingBand -> {
            // 음악 장르 데이터
            List<ProfileMetaDataDTO> musicGenreList =
                    recruitingBand.getMusicGenres().stream().map(MusicGenreMappingEntity::getGenre).toList()
                            .stream().map(musicGenre -> ProfileMetaDataDTO.builder().key(musicGenre.getkey()).value(musicGenre.getValue()).build())
                            .toList();

            // 모집 포지션 데이터
            List<ProfileMetaDataDTO> positionList =
                    recruitingBand.getRecruitingPositions().stream().map(PositionMappingEntity::getPosition).toList()
                            .stream().map(position -> ProfileMetaDataDTO.builder().key(position.getkey()).value(position.getValue()).build())
                            .toList();

            // 합주 지역 데이터
            List<DistrictDataDTO> districtList =
                    recruitingBand.getDistricts().stream().map(districtMappingEntity ->
                            DistrictDataDTO.builder()
                                    .districtId(districtMappingEntity.getDistrict().getId())
                                    .districtName(districtMappingEntity.getDistrict().getDistrictName())
                                    .build()
                    ).toList();

            return BandRecruitInfoResponseDTO.builder()
                    .bandId(recruitingBand.getId())
                    .bandName(recruitingBand.getBandName())
                    .recruitTitle(recruitingBand.getBandRecruitInfoEntity().getTitle())
                    .description(recruitingBand.getBandRecruitInfoEntity().getDescription())
                    .genres(musicGenreList)
                    .positions(positionList)
                    .districts(districtList)
                    .build();
        });
    }

    @Transactional
    public BandProfileResponseDTO getBandProfileDetail(long bandId) {
        BandEntity band = bandRepository.findBandWithRecruitInfoById(bandId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_BAND));

        // 음악 장르 데이터
        List<ProfileMetaDataDTO> musicGenreList =
                band.getMusicGenres().stream().map(MusicGenreMappingEntity::getGenre).toList()
                        .stream().map(musicGenre -> ProfileMetaDataDTO.builder().key(musicGenre.getkey()).value(musicGenre.getValue()).build())
                        .toList();

        // 모집 포지션 데이터
        List<ProfileMetaDataDTO> positionList =
                band.getRecruitingPositions().stream().map(PositionMappingEntity::getPosition).toList()
                        .stream().map(position -> ProfileMetaDataDTO.builder().key(position.getkey()).value(position.getValue()).build())
                        .toList();

        // 합주 지역 데이터
        List<DistrictDataDTO> districtList =
                band.getDistricts().stream().map(districtMappingEntity ->
                        DistrictDataDTO.builder()
                                .districtId(districtMappingEntity.getDistrict().getId())
                                .districtName(districtMappingEntity.getDistrict().getDistrictName())
                                .build()
                ).toList();

        // 밴드 소속 멤버 데이터
        List<BandMemberDTO> bandMemberList =
                band.getBandMembers().stream().map(member ->
                        BandMemberDTO.builder()
                                .userId(member.getId())
                                .nickname(member.getUser().getNickname())
                                .position(member.getPosition().getValue())
                                .build()
                ).toList();

        return BandProfileResponseDTO.builder()
                .bandId(band.getId())
                .bandName(band.getBandName())
                .leaderId(band.getUser().getId())
                .leaderNickname(band.getUser().getNickname())
                .recruitTitle(band.getBandRecruitInfoEntity().getTitle())
                .description(band.getBandRecruitInfoEntity().getDescription())
                .genres(musicGenreList)
                .positions(positionList)
                .districts(districtList)
                .members(bandMemberList)
                .build();
    }

    private List<DistrictEntity> verifyDistrict(List<Long> districts) {
        List<DistrictEntity> districtEntityList = districtRepository.findByIdIn(districts);
        if (districts.size() != districtEntityList.size()) {
            throw new BusinessException(ErrorCode.NOT_EXIST_CODE);
        }
        return districtEntityList;
    }

    @Transactional
    public void applyBand(UserEntity user, BandApplyRequestDTO bandApplyRequest) {

        // 이미 지원한 밴드
        if (bandApplyInfoRepository.findByUserId(user.getId()).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_APPLIED_BAND);
        }

        BandEntity band = bandRepository.findById(bandApplyRequest.bandId()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_BAND));

        // 본인 밴드에 지원
        if (Objects.equals(band.getUser().getId(), user.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_BAND_MEMBER);
        }

        // 밴드 멤버가 지원
        for (BandMemberEntity member : band.getBandMembers()) {
            if (Objects.equals(member.getUser().getId(), user.getId())) {
                throw new BusinessException(ErrorCode.ALREADY_BAND_MEMBER);
            }
        }

        bandApplyInfoRepository.save(BandApplyInfoEntity.builder().band(band).user(user).description(bandApplyRequest.applyDescription()).build());
    }

}

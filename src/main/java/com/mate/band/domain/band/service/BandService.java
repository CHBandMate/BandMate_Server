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

/**
 * @author : 최성민
 * @since : 2025-01-09
 * @version : 1.0
 */
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

    /**
     * 밴드 프로필을 등록한다.
     * @param user         @AuthUser
     * @param profileParam 프로필 등록 데이터
     */
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

    /**
     * 밴드명이 중복 되는지 확인하다.
     * @param bandName 밴드명
     * @return Boolean
     */
    public Boolean checkBandName(String bandName) {
        return bandRepository.findByBandName(bandName).isEmpty();
    }

    /**
     * 밴드 프로필을 수정한다.
     * @param user         @AuthUser
     * @param profileParam 프로필 수정 데이터
     */
    @Transactional
    public void editBandProfile(UserEntity user, RegisterBandProfileRequestDTO profileParam) {
        BandEntity bandEntity = bandRepository.findById(profileParam.bandId()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_BAND));
        bandEntity.updateBand(profileParam);

        MetadataEnumRepository.verifyMetadataKey(profileParam.genre(), MusicGenre.class);
        List<DistrictEntity> districts = verifyDistrict(profileParam.district());

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


        bandEntity.getMusicGenres().clear();
        bandEntity.getDistricts().clear();
        bandEntity.getMusicGenres().addAll(musicGenreMappingEntityList);
        bandEntity.getDistricts().addAll(districtMappingEntityList);
        bandEntity.getBandMembers().clear();

        List<BandMemberEntity> bandMemberEntityList = new ArrayList<>();
        for (RegisterBandMemberDTO bandMember : profileParam.bandMember()) {
            UserEntity member;
            if (bandMember.userId() == bandEntity.getUser().getId()) {    // 나 자신 일때
                member = bandEntity.getUser();
            } else {
                member = userRepository.findById(bandMember.userId()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_USER));
            }
            bandMemberEntityList.add(BandMemberEntity.builder().band(bandEntity).user(member).position(Position.valueOf(bandMember.positionCode())).build());
        }
        bandEntity.getBandMembers().addAll(bandMemberEntityList);

        if (profileParam.recruitYn()) {
            // 모집 포지션
            MetadataEnumRepository.verifyMetadataKey(profileParam.recruitPosition(), Position.class);
            List<PositionMappingEntity> positionMappingEntityList = profileParam.recruitPosition().stream().map(position ->
                    PositionMappingEntity.builder()
                            .type(MappingType.BAND)
                            .band(bandEntity)
                            .position(Position.valueOf(position))
                            .build()).toList();
            bandEntity.getRecruitingPositions().clear();
            bandEntity.getRecruitingPositions().addAll(positionMappingEntityList);

            // 모집 내용
            BandRecruitInfoEntity bandRecruitInfoEntity = bandEntity.getBandRecruitInfoEntity();
            bandRecruitInfoEntity.setTitle(profileParam.recruitTitle());
            bandRecruitInfoEntity.setDescription(profileParam.recruitDescription());
        }
    }

    /**
     * 밴드 멤버 구인 게시글을 조회한다.
     * @param districts 합주 지역
     * @param genres    음악 장르
     * @param positions 구인 포지션
     * @param recruitYn 구인 여부
     * @param pageable  페이징 정보
     * @return Page BandRecruitInfoResponseDTO
     * @TODO 리팩토링, 즐겨찾기 여부 추가
     */
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

    /**
     * 나의 밴드 프로필을 조회한다.
     * @param user @AuthUser
     * @return List BandProfileResponseDTO
     */
    @Transactional
    public List<BandProfileResponseDTO> getMyBandProfiles(UserEntity user) {
        List<BandProfileResponseDTO> myBandProfileList = new ArrayList<>();
        for (BandEntity band : bandRepository.findByUserId(user.getId())) {
            myBandProfileList.add(getBandProfileDetail(band.getId()));
        }
        return myBandProfileList;
    }

    /**
     * 밴드 상세 프로필을 조회한다.
     * @param bandId 밴드Id
     * @return BandProfileResponseDTO
     */
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

    /**
     * 지역 데이터를 검증한다.
     * @param districts 지역 정보
     * @return List DistrictEntity
     * @throws BusinessException 존재하지 않는 지역 코드
     */
    private List<DistrictEntity> verifyDistrict(List<Long> districts) {
        List<DistrictEntity> districtEntityList = districtRepository.findByIdIn(districts);
        if (districts.size() != districtEntityList.size()) {
            throw new BusinessException(ErrorCode.NOT_EXIST_CODE);
        }
        return districtEntityList;
    }

    /**
     * 현재 사용자가 밴드에 지원한다.
     * @param user             @AuthUser
     * @param bandApplyRequest 밴드 지원 내용
     */
    @Transactional
    public void applyBand(UserEntity user, BandApplyRequestDTO bandApplyRequest) {

        // 이미 지원한 밴드
        if (bandApplyInfoRepository.findByBandUserId(bandApplyRequest.bandId(), user.getId()).isPresent()) {
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

    /**
     * 나의 밴드 지원 현황을 조회한다.
     * @param user @AuthUser
     * @return List BandApplyCurrentInfoResponseDTO
     */
    public List<BandApplyCurrentInfoResponseDTO> getApplyCurrentInfo(UserEntity user) {
        List<BandApplyInfoEntity> bandApplyInfoEntity = bandApplyInfoRepository.findByUserId(user.getId());
        return bandApplyInfoEntity.stream().map(applyInfo -> {
            BandEntity band = applyInfo.getBand();
            return BandApplyCurrentInfoResponseDTO.builder()
                    .bandId(band.getId())
                    .bandName(band.getBandName())
                    .profileImageUrl(band.getProfileImageUrl())
                    .openYn(applyInfo.isOpenYn())
                    .build();
        }).toList();
    }

    /**
     * 나의 밴드에 지원한 지원자 내역을 조회한다.
     * @param user @AuthUser
     * @param bandId 밴드Id
     * @return List BandApplicantResponseDTO
     */
    public List<BandApplicantResponseDTO> getApplicantInfo(UserEntity user, long bandId) {
        List<BandApplyInfoEntity> applyInfoList = bandApplyInfoRepository.findUserByBandId(user.getId(), bandId);
        if (applyInfoList.isEmpty()) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED_DATA);
        }
        return applyInfoList.stream().map(apply -> {
            UserEntity applyUser = apply.getUser();
            return BandApplicantResponseDTO.builder()
                    .userId(applyUser.getId())
                    .profileImageUrl(applyUser.getProfileImageUrl())
                    .nickname(applyUser.getNickname())
                    .description(apply.getDescription())
                    .openYn(apply.isOpenYn())
                    .build();
        }).toList();
    }

}

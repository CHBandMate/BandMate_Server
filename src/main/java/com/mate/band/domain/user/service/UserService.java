package com.mate.band.domain.user.service;

import com.mate.band.domain.band.entity.BandEntity;
import com.mate.band.domain.band.entity.BandMemberEntity;
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
import com.mate.band.domain.user.dto.RegisterUserProfileRequestDTO;
import com.mate.band.domain.user.dto.UserInviteRequestDTO;
import com.mate.band.domain.user.dto.UserProfileBandInfoDTO;
import com.mate.band.domain.user.dto.UserProfileResponseDTO;
import com.mate.band.domain.user.entity.UserEntity;
import com.mate.band.domain.user.entity.UserInviteInfoEntity;
import com.mate.band.domain.user.repository.UserInviteInfoRepository;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BandRepository bandRepository;
    private final DistrictRepository districtRepository;
    private final PositionMappingRepository positionMappingRepository;
    private final MusicGenreMappingRepository musicGenreMappingRepository;
    private final DistrictMappingRepository districtMappingRepository;
    private final UserInviteInfoRepository userInviteInfoRepository;

    public Optional<UserEntity> findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    // TODO 리팩토링, 즐겨찾기 여부 추가
    @Transactional
    public Page<UserProfileResponseDTO> getUserProfileList(UserEntity authUser, String districts, String genres, String positions, Pageable pageable) {
        List<Long> districtParam = districts.equals("ALL") ? new ArrayList<>() : Arrays.stream(districts.replaceAll(" ", "").split(",")).map(Long::valueOf).toList();
        List<String> genreParam = genres.equals("ALL") ? new ArrayList<>() : Arrays.stream(genres.replaceAll(" ", "").split(",")).toList();
        List<String> positionParam = positions.equals("ALL") ? new ArrayList<>() : Arrays.stream(positions.replaceAll(" ", "").split(",")).toList();
        Page<UserEntity> userList = userRepository.findUserList(authUser, districtParam, genreParam, positionParam, pageable);

        return userList.map(user -> {
            // 음악 장르 데이터
            List<ProfileMetaDataDTO> musicGenreList =
                    user.getMusicGenres().stream().map(MusicGenreMappingEntity::getGenre).toList()
                            .stream().map(musicGenre -> ProfileMetaDataDTO.builder().key(musicGenre.getkey()).value(musicGenre.getValue()).build())
                            .toList();

            // 모집 포지션 데이터
            List<ProfileMetaDataDTO> positionList =
                    user.getPositions().stream().map(PositionMappingEntity::getPosition).toList()
                            .stream().map(position -> ProfileMetaDataDTO.builder().key(position.getkey()).value(position.getValue()).build())
                            .toList();

            // 합주 지역 데이터
            List<DistrictDataDTO> districtList =
                    user.getDistricts().stream().map(districtMappingEntity ->
                            DistrictDataDTO.builder()
                                    .districtId(districtMappingEntity.getDistrict().getId())
                                    .districtName(districtMappingEntity.getDistrict().getDistrictName())
                                    .build()
                    ).toList();

            return UserProfileResponseDTO.builder()
                    .userId(user.getId())
                    .nickname(user.getNickname())
                    .introduction(user.getIntroduction())
                    .genres(musicGenreList)
                    .positions(positionList)
                    .districts(districtList)
                    .build();
        });
    }

    // TODO 리팩토링
    @Transactional
    public void registerUserProfile(UserEntity user, RegisterUserProfileRequestDTO profileParam) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        if (userEntity.getRole() != Role.NOT_REGISTERED) {
            throw new BusinessException(ErrorCode.REGISTERED_USER);
        }

        MetadataEnumRepository.verifyMetadataKey(profileParam.position(), Position.class);
        MetadataEnumRepository.verifyMetadataKey(profileParam.genre(), MusicGenre.class);
        List<DistrictEntity> districts = verifyDistrict(profileParam.district());
        userEntity.registryUser(profileParam);

        List<PositionMappingEntity> positionMappingEntityList = profileParam.position().stream().map(position ->
                PositionMappingEntity.builder()
                        .type(MappingType.USER)
                        .user(userEntity)
                        .position(Position.valueOf(position))
                        .build()).toList();

        List<MusicGenreMappingEntity> musicGenreMappingEntityList = profileParam.genre().stream().map(genre ->
                MusicGenreMappingEntity.builder()
                        .type(MappingType.USER)
                        .user(userEntity)
                        .genre(MusicGenre.valueOf(genre))
                        .build()).toList();

        List<DistrictMappingEntity> districtMappingEntityList = districts.stream().map(district ->
                DistrictMappingEntity.builder()
                        .type(MappingType.USER)
                        .user(userEntity)
                        .district(district)
                        .build()).toList();

        positionMappingRepository.saveAll(positionMappingEntityList);
        musicGenreMappingRepository.saveAll(musicGenreMappingEntityList);
        districtMappingRepository.saveAll(districtMappingEntityList);
    }

    private List<DistrictEntity> verifyDistrict(List<Long> districts) {
        List<DistrictEntity> districtEntityList = districtRepository.findByIdIn(districts);
        if (districts.size() != districtEntityList.size()) {
            throw new BusinessException(ErrorCode.NOT_EXIST_CODE);
        }
        return districtEntityList;
    }

    @Transactional
    public UserProfileResponseDTO getUserProfile(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_USER));

        // 음악 장르 데이터
        List<ProfileMetaDataDTO> musicGenreList =
                user.getMusicGenres().stream().map(MusicGenreMappingEntity::getGenre).toList()
                        .stream().map(musicGenre -> ProfileMetaDataDTO.builder().key(musicGenre.getkey()).value(musicGenre.getValue()).build())
                        .toList();

        // 모집 포지션 데이터
        List<ProfileMetaDataDTO> positionList =
                user.getPositions().stream().map(PositionMappingEntity::getPosition).toList()
                        .stream().map(position -> ProfileMetaDataDTO.builder().key(position.getkey()).value(position.getValue()).build())
                        .toList();

        // 합주 지역 데이터
        List<DistrictDataDTO> districtList =
                user.getDistricts().stream().map(districtMappingEntity ->
                        DistrictDataDTO.builder()
                                .districtId(districtMappingEntity.getDistrict().getId())
                                .districtName(districtMappingEntity.getDistrict().getDistrictName())
                                .build()
                ).toList();

        List<UserProfileBandInfoDTO> bandInfoList =
                user.getBands().stream().map(band ->
                        UserProfileBandInfoDTO.builder()
                                .bandId(band.getId())
                                .bandName(band.getBandName())
                                .profileImageUrl(band.getProfileImageUrl())
                                .build()
                ).toList();

        return UserProfileResponseDTO.builder()
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .nickname(user.getNickname())
                .introduction(user.getIntroduction())
                .genres(musicGenreList)
                .positions(positionList)
                .districts(districtList)
                .bandInfos(bandInfoList)
                .build();
    }

    public void inviteUser(UserInviteRequestDTO inviteParam) {
        if (userInviteInfoRepository.findByBandUserId(inviteParam.bandId(), inviteParam.userId()).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_INVITED_USER);
        }

        BandEntity band = bandRepository.findById(inviteParam.bandId()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_BAND));
        for (BandMemberEntity member : band.getBandMembers()) {
            if (member.getUser().getId().equals(inviteParam.userId())) {
                throw new BusinessException(ErrorCode.ALREADY_BAND_MEMBER);
            }
        }

        userInviteInfoRepository.save(
                UserInviteInfoEntity.builder()
                        .band(band)
                        .user(userRepository.findById(inviteParam.userId()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_USER)))
                        .description(inviteParam.description())
                        .openYn(false)
                        .deleteYn(false)
                        .build()
        );
    }

}

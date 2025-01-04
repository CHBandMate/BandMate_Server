package com.mate.band.domain.user.service;

import com.mate.band.domain.common.constants.ProfileMetadata;
import com.mate.band.domain.common.repository.CommonCodeRepository;
import com.mate.band.domain.user.dto.MetaDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileMetadataService {

    private final CommonCodeRepository commonCodeRepository;

    public MetaDataResponse getProfileMetadata() {
        return MetaDataResponse.builder()
                .musicGenre(commonCodeRepository.findValueByCodeGroup(ProfileMetadata.MUSIC_GENRE.getCodeGroup()))
                .position(commonCodeRepository.findValueByCodeGroup(ProfileMetadata.BAND_POSITION.getCodeGroup()))
                .snsPlatform(commonCodeRepository.findValueByCodeGroup(ProfileMetadata.SNS_PLATFORM.getCodeGroup()))
                .build();
    }

}

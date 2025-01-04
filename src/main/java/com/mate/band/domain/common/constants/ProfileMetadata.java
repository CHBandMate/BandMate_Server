package com.mate.band.domain.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProfileMetadata {
    MUSIC_GENRE("music_genre"),
    BAND_POSITION("band_position"),
    SNS_PLATFORM("sns_platform")
    ;

    private final String codeGroup;
}

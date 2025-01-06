package com.mate.band.domain.profile.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Metadata {
    MUSIC_GENRE("music_genre"),
    BAND_POSITION("band_position"),
    SNS_PLATFORM("sns_platform"),
    REGION("region")
    ;

    private final String codeGroup;
}

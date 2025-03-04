package com.mate.band.domain.metadata.constants;

import com.mate.band.domain.common.EnumModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SNS 플랫폼 ENUM
 * @author : 최성민
 * @since : 2025-01-08
 * @version : 1.0
 */
@AllArgsConstructor
@Getter
public enum SnsPlatform implements EnumModel {
    YOUTUBE("YOUTUBE", "유튜브"),
    INSTAGRAM("INSTAGRAM", "인스타그램"),
    SOUND_CLOUD("SOUND_CLOUD", "사운드클라우드")
    ;

    String key, value;

    @Override
    public String getkey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}

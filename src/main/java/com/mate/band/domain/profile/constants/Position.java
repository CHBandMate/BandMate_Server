package com.mate.band.domain.profile.constants;

import com.mate.band.domain.common.EnumModel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Position implements EnumModel {
    GUITAR("GUITAR", "기타"),
    BASS("BASS", "베이스"),
    DRUM("DRUM", "드럼"),
    KEYBOARD("KEYBOARD", "건반"),
    VOCAL("VOCAL", "보컬"),
    ETC("ETC", "그 외"),
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

package com.mate.band.domain.metadata.constants;

import com.mate.band.domain.common.EnumModel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 밴드 포지션 ENUM
 * @author : 최성민
 * @since : 2025-01-08
 * @version : 1.0
 */
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

    public static List<Position> values(List<String> param) {
        List<Position> result = new ArrayList<>();
        for (String s : param) {
            result.add(Position.valueOf(s));
        }
        return result;
    }

}

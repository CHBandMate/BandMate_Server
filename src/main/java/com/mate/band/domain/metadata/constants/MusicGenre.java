package com.mate.band.domain.metadata.constants;

import com.mate.band.domain.common.EnumModel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 음악 장르 ENUM
 * @author : 최성민
 * @since : 2025-01-08
 * @version : 1.0
 */
@AllArgsConstructor
public enum MusicGenre implements EnumModel {
    POP("POP", "팝"),
    BALLAD("BALLAD", "발라드"),
    INDIE("INDIE", "인디음악"),
    HIPHOP("HIPHOP", "힙합"),
    KPOP("KPOP", "K-POP"),
    JPOP("JPOP", "J-POP"),
    TROT("TROT", "트로트"),
    ELECTRONIC("ELECTRONIC", "일렉트로닉"),
    ROCK("ROCK", "락"),
    METAL("METAL", "메탈"),
    RNB("RNB", "R&B"),
    JAZZ("JAZZ", "재즈"),
    CLASSIC("CLASSIC", "클래식"),
    FUNK("FUNK", "펑크"),
    ETC("ETC", "그외")
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

    public static List<MusicGenre> values(List<String> param) {
        List<MusicGenre> result = new ArrayList<>();
        for (String s : param) {
            result.add(MusicGenre.valueOf(s));
        }
        return result;
    }

}

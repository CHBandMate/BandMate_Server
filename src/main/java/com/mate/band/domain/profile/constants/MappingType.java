package com.mate.band.domain.profile.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MappingType {
    BAND('B'),
    USER('U'),
    ;

    char value;
}

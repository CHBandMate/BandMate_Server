package com.mate.band.domain.metadata.repository;

import com.mate.band.domain.common.EnumModel;
import com.mate.band.global.exception.BusinessException;
import com.mate.band.global.exception.ErrorCode;

import java.util.List;

public class MetadataEnumRepository {
    public static void verifyMetadataKey(List<String> keyList, Class<? extends EnumModel> enumClass) {
        EnumModel[] enumConstants = enumClass.getEnumConstants();
        for (String key : keyList) {
            boolean isValid = false;
            for (EnumModel enumConstant : enumConstants) {
                if (enumConstant.getkey().equals(key)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                throw new BusinessException(ErrorCode.NOT_EXIST_CODE);
            }
        }
    }
}

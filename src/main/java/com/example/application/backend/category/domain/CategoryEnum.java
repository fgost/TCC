package com.example.application.backend.category.domain;

import com.example.application.exception.util.ExceptionUtils;
import com.example.application.exception.util.MessageResource;

import java.util.Arrays;

public enum CategoryEnum {

    PREVENTIVE((short) 0),
    CORRECTIVE((short) 1),
    PREDICTIVE((short) 2),
    SCHEDULED((short) 3),
    EMERGENCY((short) 4);

    private final short value;

    public static CategoryEnum fromValue(Short categoryCode) {
        var message = MessageResource.getInstance().getMessage("category.invalid");
        return Arrays.stream(CategoryEnum.values())
                .filter(it -> Short.valueOf(it.getValue()).equals(categoryCode))
                .findFirst()
                .orElseThrow(() -> ExceptionUtils.buildBadRequestException(message));
    }

    public short getValue() {
        return value;
    }

    CategoryEnum(short value) {
        this.value = value;
    }
}


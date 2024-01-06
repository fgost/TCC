package com.example.application.backend.car.domain;

import com.example.application.exception.util.ExceptionUtils;
import com.example.application.exception.util.MessageResource;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CarTypeEnum {
    HATCHBACK((short) 0),
    SEDAN((short) 1),
    SUV((short) 2),
    PICKUP_TRUCK((short) 3);

    private final short value;

    public static CarTypeEnum fromValue(Short carTypeCode) {
        var message = MessageResource.getInstance().getMessage("car.type.invalid");
        return Arrays.stream(CarTypeEnum.values())
                .filter(it -> Short.valueOf(it.getValue()).equals(carTypeCode))
                .findFirst()
                .orElseThrow(() -> ExceptionUtils.buildBadRequestException(message));
    }

    CarTypeEnum(short value) {
        this.value = value;
    }
}

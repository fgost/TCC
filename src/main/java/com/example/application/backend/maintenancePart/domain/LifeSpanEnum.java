package com.example.application.backend.maintenancePart.domain;

import com.example.application.exception.util.ExceptionUtils;
import com.example.application.exception.util.MessageResource;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum LifeSpanEnum {
    KM((short) 0),
    MES((short) 1),
    ANO((short) 2),
    DIA((short) 3);

    private final short value;

    public static LifeSpanEnum fromValue(Short statusCode) {
        var message = MessageResource.getInstance().getMessage("life.span.invalid");
        return Arrays.stream(LifeSpanEnum.values())
                .filter(it -> Short.valueOf(it.getValue()).equals(statusCode))
                .findFirst()
                .orElseThrow(() -> ExceptionUtils.buildBadRequestException(message));
    }

    LifeSpanEnum(short value) {
        this.value = value;
    }
}

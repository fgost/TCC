package com.example.application.backend.maintenancePart.domain;

import com.example.application.exception.util.ExceptionUtils;
import com.example.application.exception.util.MessageResource;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DetailedMaintenanceEnum {
    DETAILED((short) 0),
    SIMPLE((short) 1);

    private final short value;

    public static DetailedMaintenanceEnum fromValue(Short statusCode) {
        var message = MessageResource.getInstance().getMessage("life.span.invalid");
        return Arrays.stream(DetailedMaintenanceEnum.values())
                .filter(it -> Short.valueOf(it.getValue()).equals(statusCode))
                .findFirst()
                .orElseThrow(() -> ExceptionUtils.buildBadRequestException(message));
    }

    DetailedMaintenanceEnum(short value) {
        this.value = value;
    }
}

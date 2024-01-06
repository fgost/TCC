package com.example.application.backend.maintenancePart.domain;

import com.example.application.exception.util.ExceptionUtils;
import com.example.application.exception.util.MessageResource;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MaintenancePartStatusEnum {
    NEW((short) 0),
    USED((short) 1),
    HALF_LIFE((short) 2),
    URGENT_REPLACEMENT((short) 3);

    private final short value;

    public static MaintenancePartStatusEnum fromValue(Short statusCode) {
        var message = MessageResource.getInstance().getMessage("maintenance.part.status.invalid");
        return Arrays.stream(MaintenancePartStatusEnum.values())
                .filter(it -> Short.valueOf(it.getValue()).equals(statusCode))
                .findFirst()
                .orElseThrow(() -> ExceptionUtils.buildBadRequestException(message));
    }

    MaintenancePartStatusEnum(short value) {
        this.value = value;
    }

}


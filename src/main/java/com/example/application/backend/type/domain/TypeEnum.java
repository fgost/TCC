package com.example.application.backend.type.domain;

import com.example.application.exception.util.ExceptionUtils;
import com.example.application.exception.util.MessageResource;

import java.util.Arrays;

public enum TypeEnum {

    MECHANICS((short) 0),
    ELECTRIC((short) 1),
    AIRCONDITIONING((short) 2),
    FLUIDS((short) 3),
    OTHERSPECIALTIES((short) 4);

    private final short value;

    public static TypeEnum fromValue(Short typeCode) {
        var message = MessageResource.getInstance().getMessage("type.invalid");
        return Arrays.stream(TypeEnum.values())
                .filter(it -> Short.valueOf(it.getValue()).equals(typeCode))
                .findFirst()
                .orElseThrow(() -> ExceptionUtils.buildBadRequestException(message));
    }

    TypeEnum(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }
}

package com.example.application.backend.type.domain;

import com.example.application.exception.util.ExceptionUtils;
import com.example.application.exception.util.MessageResource;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TypeEnum {

    PREVENTIVA((short) 0),
    PREDITIVA((short) 1),
    CORRETIVA((short) 2);

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

}

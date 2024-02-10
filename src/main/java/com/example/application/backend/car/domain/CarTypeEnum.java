package com.example.application.backend.car.domain;

import com.example.application.exception.domain.BadRequestException;
import com.example.application.exception.util.ExceptionUtils;
import com.example.application.exception.util.MessageResource;
import lombok.Getter;

import java.util.Arrays;

/**
 * Enum representing different types of cars. Each car type has a corresponding numeric value. The numeric values
 * associated with each car type are used for internal representation. The `fromValue` method is provided to convert
 * a numeric value to its corresponding enum constant. If an invalid numeric value is provided, a bad request exception
 * is thrown.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 */
@Getter
public enum CarTypeEnum {
    HATCHBACK((short) 0),
    SEDAN((short) 1),
    SUV((short) 2),
    PICKUP_TRUCK((short) 3);

    private final short value;

    /**
     * Gets the CarTypeEnum constant based on the provided numeric value. If the provided numeric value is not valid,
     * a bad request exception is thrown.
     *
     * @param carTypeCode - Numeric value representing the car type.
     *
     * @return CarTypeEnum - The corresponding CarTypeEnum constant.
     *
     * @throws BadRequestException if the numeric value is not valid.
     */
    public static CarTypeEnum fromValue(Short carTypeCode) {
        var message = MessageResource.getInstance().getMessage("car.type.invalid");
        return Arrays.stream(CarTypeEnum.values())
                .filter(it -> Short.valueOf(it.getValue()).equals(carTypeCode))
                .findFirst()
                .orElseThrow(() -> ExceptionUtils.buildBadRequestException(message));
    }

    /**
     * Constructor for the CarTypeEnum.
     *
     * @param value - The numeric value associated with the car type.
     */
    CarTypeEnum(short value) {
        this.value = value;
    }
}

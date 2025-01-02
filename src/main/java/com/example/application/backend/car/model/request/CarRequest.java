package com.example.application.backend.car.model.request;

import com.example.application.backend.car.domain.CarTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Represents the request DTO for creating or updating a car entity. The CarRequest class encapsulates the request data
 * for a car entity. It includes validation annotations such as @NotBlank, @NotNull, and @Size to enforce constraints
 * on the input data.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 *
 * @see lombok.Getter - Lombok annotation for generating getter methods
 * @see lombok.Setter - Lombok annotation for generating setter methods
 * @see lombok.Builder - Lombok annotation for generating a builder
 * @see lombok.NoArgsConstructor - Lombok annotation for generating a no-args constructor
 * @see lombok.AllArgsConstructor - Lombok annotation for generating an all-args constructor
 * @see com.example.application.backend.car.domain.CarTypeEnum - Enum representing the type of the car
 * @see jakarta.validation.constraints.NotBlank - Jakarta Bean Validation annotation for ensuring a non-blank value
 * @see jakarta.validation.constraints.NotNull - Jakarta Bean Validation annotation for ensuring a non-null value
 * @see jakarta.validation.constraints.Size - Jakarta Bean Validation annotation for ensuring a value's size is within
 * a specified range
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    @NotBlank(message = "{car.model.not.null}")
    @Size(max = 100, message = "{car.model.max.size}")
    private String carModel;

    @NotBlank(message = "{car.automaker.not.null}")
    @Size(max = 100, message = "{car.automaker.max.size}")
    private String autoMaker;

    @NotBlank(message = "{car.year.not.null}")
    @Size(max = 100, message = "{car.year.max.size}")
    private String year;

    @NotBlank(message = "{car.color.not.null}")
    @Size(max = 100, message = "{car.color.max.size}")
    private String color;

    @NotNull(message = "{car.type.not.null}")
    private CarTypeEnum type;

    @NotNull(message = "{car.mileage.not.null}")
    private double mileage;

    @NotBlank(message = "{car.motor.not.null}")
    @Size(max = 100, message = "{car.motor.max.size}")
    private String motor;

    @NotBlank(message = "{car.license.plate.not.null}")
    @Size(max = 100, message = "{car.license.plate.max.size}")
    private String licencePlate;
}

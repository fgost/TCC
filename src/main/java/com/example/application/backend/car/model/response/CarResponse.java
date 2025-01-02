package com.example.application.backend.car.model.response;

import com.example.application.backend.car.domain.CarTypeEnum;
import lombok.*;

/**
 * Represents the response DTO for a car entity. The CarResponse class encapsulates the response data for a car
 * entity. It provides accessors (getters and setters) for various attributes of the car.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 *
 * @see lombok.Getter - Lombok annotation for generating getter methods
 * @see lombok.Setter - Lombok annotation for generating setter methods
 * @see lombok.Builder - Lombok annotation for generating a builder
 * @see lombok.NoArgsConstructor - Lombok annotation for generating a no-args constructor
 * @see lombok.AllArgsConstructor - Lombok annotation for generating an all-args constructor
 * @see com.example.application.backend.car.domain.CarTypeEnum - Enum representing the type of the car
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {
    private String code;
    private String carModel;
    private String autoMaker;
    private String year;
    private String color;
    private CarTypeEnum type;
    private double mileage;
    private String motor;
    private String licensePlate;
}

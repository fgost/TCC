package com.example.application.backend.car.model.request;

import com.example.application.backend.car.domain.CarTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
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

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setAutoMaker(String autoMaker) {
        this.autoMaker = autoMaker;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setType(CarTypeEnum type) {
        this.type = type;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }
}

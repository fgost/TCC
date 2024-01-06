package com.example.application.backend.car.model.response;

import com.example.application.backend.car.domain.CarTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

    public void setCode(String code) {
        this.code = code;
    }

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

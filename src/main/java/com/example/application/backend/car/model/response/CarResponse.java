package com.example.application.backend.car.model.response;

import com.example.application.backend.car.domain.CarTypeEnum;
import lombok.*;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getAutoMaker() {
        return autoMaker;
    }

    public void setAutoMaker(String autoMaker) {
        this.autoMaker = autoMaker;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CarTypeEnum getType() {
        return type;
    }

    public void setType(CarTypeEnum type) {
        this.type = type;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }
}

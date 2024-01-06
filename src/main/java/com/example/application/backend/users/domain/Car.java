package com.example.application.backend.users.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Car {

    @NotBlank
    private String carModel;

    @NotBlank
    private String year;

    @NotBlank
    private String code;

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

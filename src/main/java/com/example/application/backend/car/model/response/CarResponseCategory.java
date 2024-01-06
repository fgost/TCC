package com.example.application.backend.car.model.response;

import com.example.application.backend.car.domain.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CarResponseCategory {

    @JsonProperty(value = "carName")
    private String carModel;
    @JsonProperty(value = "categories")
    private Set<Category> categories;

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}

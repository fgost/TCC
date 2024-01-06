package com.example.application.backend.users.model.response;

import com.example.application.backend.users.domain.Car;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
public class UserResponseCar {
    @JsonProperty(value = "user")
    private String fullName;

    @JsonProperty(value = "cars")
    private Set<Car> cars;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }
}

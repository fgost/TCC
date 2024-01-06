package com.example.application.backend.users.model.response;

import com.example.application.backend.car.model.response.CarResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @JsonProperty("idExterno")
    private String code;
    private String nomeCompleto;
    @JsonProperty("nome")
    private String name;
    @JsonProperty("sobrenome")
    private String lastName;
    private String email;
    @JsonProperty("cars")
    private Set<CarResponse> cars = new HashSet<>();

    public void setCode(String code) {
        this.code = code;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCars(Set<CarResponse> cars) {
        this.cars = cars;
    }
}

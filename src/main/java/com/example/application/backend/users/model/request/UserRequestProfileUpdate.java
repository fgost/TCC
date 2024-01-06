package com.example.application.backend.users.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestProfileUpdate {
    @JsonProperty(value = "perfilIdExterno")
    @NotBlank
    private String code;

    public void setCode(String code) {
        this.code = code;
    }
}

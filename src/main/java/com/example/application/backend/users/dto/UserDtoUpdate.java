package com.example.application.backend.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoUpdate {
    @NotBlank(message = "{user.name.not.null}")
    @Size(max = 100, message = "{user.name.max.size}")
    @JsonProperty(value = "nome")
    private String name;

    @NotBlank(message = "{user.lastName.not.null}")
    @Size(max = 100, message = "{user.lastName.max.size}")
    @JsonProperty(value = "sobrenome")
    private String lastName;

    @NotBlank(message = "{user.email.not.null}")
    @Size(max = 100, message = "{user.email.max.size}")
    private String email;

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

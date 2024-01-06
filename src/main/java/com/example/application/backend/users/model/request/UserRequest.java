package com.example.application.backend.users.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "{user.name.not.null}")
    @Size(max = 100, message = "{user.name.max.size}")
    @JsonProperty(value = "nome")
    private String name;

    @NotBlank(message = "{user.last.name.not.null}")
    @Size(max = 100, message = "{user.lastName.max.size}")
    @JsonProperty(value = "sobrenome")
    private String lastName;

    @NotBlank(message = "{user.email.not.null}")
    @Size(max = 100, message = "{user.email.max.size}")
    private String email;

    @NotBlank(message = "{user.password.not.null}")
    @Size(max = 100, message = "{user.password.max.size}")
    @JsonProperty(value = "senha")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

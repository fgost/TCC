package com.example.application.backend.users.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseSummary {

    @JsonProperty(value = "idExterno")
    private String code;
    private String nomeCompleto;
    @JsonProperty(value = "nome")
    private String name;
    @JsonProperty(value = "sobrenome")
    private String lastName;
    private String email;

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
}

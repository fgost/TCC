package com.example.application.backend.users.model.response;

import com.example.application.backend.users.domain.Preference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponsePreference {

    @JsonProperty(value = "usuarioNomeCompleto")
    private String fullName;

    @JsonProperty(value = "preferencias")
    private Set<Preference> preferences;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }

}

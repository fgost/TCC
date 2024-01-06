package com.example.application.backend.users.model.response;

import com.example.application.backend.users.domain.Preference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
public class UserResponsePreference {
    @JsonProperty(value = "usuarioNomeCompleto")
    private String fullName;

    @JsonProperty(value = "preferencias")
    private Set<Preference> preferences;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }

}

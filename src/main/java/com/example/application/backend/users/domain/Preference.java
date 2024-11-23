package com.example.application.backend.users.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Preference {

    @NotBlank
    @JsonProperty(value = "key")
    private String preferenceKey;

    @NotBlank
    @JsonProperty(value = "value")
    private String preferenceValue;

    public void setPreferenceKey(String preferenceKey) {
        this.preferenceKey = preferenceKey;
    }

    public void setPreferenceValue(String preferenceValue) {
        this.preferenceValue = preferenceValue;
    }

    public @NotBlank String getPreferenceKey() {
        return preferenceKey;
    }

    public @NotBlank String getPreferenceValue() {
        return preferenceValue;
    }
}

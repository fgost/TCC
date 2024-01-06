package com.example.application.utils.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class OnlyCodeDto {
    @NotBlank(message = "{not.blank}")
    @Size(max = 36, min = 36, message = "{size}")
    @JsonProperty("idExterno")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

package com.example.application.backend.users.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Car {

    @NotBlank
    private String carModel;
    @NotBlank
    private String year;
    @NotBlank
    private String code;
}

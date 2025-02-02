package com.example.application.backend.part.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartRequest {
    @NotNull(message = "{type.name.not.null}")
    private String partName;

    @NotNull(message = "{type.name.not.null}")
    private long component;
}

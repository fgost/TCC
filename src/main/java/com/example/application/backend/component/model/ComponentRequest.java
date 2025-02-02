package com.example.application.backend.component.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentRequest {
    @NotNull(message = "{type.name.not.null}")
    private String componentName;
}

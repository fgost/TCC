package com.example.application.backend.component.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentResponse {
    private String code;
    private String componentName;
}

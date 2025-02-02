package com.example.application.backend.part.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartResponse {
    private String code;
    private String typeName;
    private long component;

}

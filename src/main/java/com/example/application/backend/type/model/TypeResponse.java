package com.example.application.backend.type.model;

import com.example.application.backend.type.domain.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeResponse {
    private String code;
    private TypeEnum typeName;

    public void setCode(String code) {
        this.code = code;
    }

    public void setTypeName(TypeEnum typeName) {
        this.typeName = typeName;
    }
}

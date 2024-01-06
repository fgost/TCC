package com.example.application.backend.type.model;

import com.example.application.backend.type.domain.TypeEnum;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeResponse {
    private String code;
    private TypeEnum typeName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TypeEnum getTypeName() {
        return typeName;
    }

    public void setTypeName(TypeEnum typeName) {
        this.typeName = typeName;
    }
}

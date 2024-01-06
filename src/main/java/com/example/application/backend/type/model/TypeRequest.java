package com.example.application.backend.type.model;

import com.example.application.backend.type.domain.TypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeRequest {
    @NotNull(message = "{type.name.not.null}")
    private TypeEnum typeName;

    public TypeEnum getTypeName() {
        return typeName;
    }

    public void setTypeName(TypeEnum typeName) {
        this.typeName = typeName;
    }
}

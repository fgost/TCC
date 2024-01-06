package com.example.application.backend.type.model;

import com.example.application.backend.type.domain.TypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeRequest {
    @NotNull(message = "{type.name.not.null}")
    private TypeEnum typeName;

    public void setTypeName(TypeEnum typeName) {
        this.typeName = typeName;
    }
}

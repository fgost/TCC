package com.example.application.backend.category.domain;

import com.example.application.backend.type.domain.TypeEnum;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Type {

    @NotBlank
    private TypeEnum typeName;

    public TypeEnum getTypeName() {
        return typeName;
    }

    public void setTypeName(TypeEnum typeName) {
        this.typeName = typeName;
    }

    public Type(TypeEnum typeName) {
        this.typeName = typeName;
    }
}

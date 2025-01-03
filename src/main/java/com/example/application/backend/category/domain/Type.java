package com.example.application.backend.category.domain;

import com.example.application.backend.type.domain.TypeEnum;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Type {

    @NotBlank
    private TypeEnum typeName;
}

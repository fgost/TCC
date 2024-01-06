package com.example.application.backend.car.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Category {

    @NotBlank
    private String categoryName;

    public static Builder builder() {
        return new Builder();
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static class Builder {
        private String categoryName;

        private Builder() {
            // Construtor privado
        }

        public Builder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Category build() {
            Category category = new Category();
            category.categoryName = this.categoryName;
            return category;
        }
    }
}
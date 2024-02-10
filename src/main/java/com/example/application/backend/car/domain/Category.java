package com.example.application.backend.car.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Represents a category for a car. This class is used as an embedded entity within other entities.
 * Categories have a unique name and are used to classify cars.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 *
 * @see CarEntity - Entity representing a car that can have one or more categories
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class Category {

    @NotBlank
    private String categoryName;

    /**
     * Default builder method to create a new instance of the {@link Category.Builder}.
     *
     * @return Builder - A new instance of the builder for creating a {@link Category} object.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for creating instances of the {@link Category} class.
     */
    public static class Builder {
        private String categoryName;

        private Builder() {
        }

        /**
         * Setter method for the category name in the builder.
         *
         * @param categoryName - String representing the name of the category.
         *
         * @return Builder - The builder instance for method chaining.
         */
        public Builder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        /**
         * Builds and returns a new instance of the {@link Category} class using the provided builder settings.
         *
         * @return Category - A new instance of the {@link Category} class.
         */
        public Category build() {
            Category category = new Category();
            category.categoryName = this.categoryName;
            return category;
        }
    }
}
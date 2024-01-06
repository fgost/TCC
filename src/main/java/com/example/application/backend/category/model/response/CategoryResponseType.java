package com.example.application.backend.category.model.response;

import com.example.application.backend.category.domain.CategoryEnum;
import com.example.application.backend.category.domain.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
public class CategoryResponseType {
    @JsonProperty(value = "categoryName")
    private CategoryEnum category;
    @JsonProperty(value = "types")
    private Set<Type> types;

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public void setTypes(Set<Type> types) {
        this.types = types;
    }

    public CategoryResponseType(CategoryEnum category, Set<Type> types) {
        this.category = category;
        this.types = types;
    }
}

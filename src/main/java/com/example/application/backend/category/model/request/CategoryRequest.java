package com.example.application.backend.category.model.request;

import com.example.application.backend.category.domain.CategoryEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "{category.not.null}")
    private CategoryEnum category;

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public CategoryRequest(CategoryEnum category) {
        this.category = category;
    }
}

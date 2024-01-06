package com.example.application.backend.category.model.response;

import com.example.application.backend.category.domain.CategoryEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
public class CategoryResponse {
    private String code;
    private CategoryEnum category;

    public void setCode(String code) {
        this.code = code;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public CategoryResponse(String code, CategoryEnum category) {
        this.code = code;
        this.category = category;
    }
}

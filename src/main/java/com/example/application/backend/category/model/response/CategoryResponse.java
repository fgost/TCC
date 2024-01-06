package com.example.application.backend.category.model.response;

import com.example.application.backend.category.domain.CategoryEnum;
import lombok.*;


@Builder
@NoArgsConstructor
public class CategoryResponse {
    private String code;
    private CategoryEnum category;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public CategoryResponse(String code, CategoryEnum category) {
        this.code = code;
        this.category = category;
    }
}

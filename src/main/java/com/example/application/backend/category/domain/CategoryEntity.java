package com.example.application.backend.category.domain;

import com.example.application.backend.type.domain.TypeEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private CategoryEnum category;

    @ManyToMany
    @JoinTable(
            name = "categories_types",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private List<TypeEntity> types;

    @PrePersist
    private void setCode() {
        this.code = UUID.randomUUID().toString();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public void setTypes(List<TypeEntity> types) {
        this.types = types;
    }

    public CategoryEntity(String code, CategoryEnum category, List<TypeEntity> types) {
        this.code = code;
        this.category = category;
        this.types = types;
    }
}

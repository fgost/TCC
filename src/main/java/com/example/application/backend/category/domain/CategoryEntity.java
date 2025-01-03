package com.example.application.backend.category.domain;

import com.example.application.backend.type.domain.TypeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}

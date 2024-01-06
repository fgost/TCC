package com.example.application.backend.category.repository;

import com.example.application.backend.category.domain.CategoryEntity;
import com.example.application.backend.category.domain.CategoryEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByCode(String code);
    List<CategoryEntity> findAll();
    @Query("SELECT id FROM CategoryEntity")
    List<Integer> findAllIds();
}
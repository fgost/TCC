package com.example.application.backend.category;

import com.example.application.backend.category.domain.CategoryEnum;
import com.example.application.backend.category.model.request.CategoryRequest;
import com.example.application.backend.category.model.response.CategoryResponse;
import com.example.application.backend.category.model.response.CategoryResponseType;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/carview/v1/category")
@Validated
public class CategoryController {
    private final CategoryFacade categoryFacade;

    public CategoryController(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll(
            @RequestParam(name = "category", defaultValue = "") CategoryEnum category) {
        var categories = categoryFacade.findAll(category);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findByCode(@PathVariable(name = "id") String code) {
        var response = categoryFacade.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/types")
    public ResponseEntity<CategoryResponseType> getTypes(@PathVariable(name = "id") String code) {
        var response = categoryFacade.getTypes(code);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> insert(
            @Valid @RequestBody CategoryRequest categoryRequest,
            HttpServletResponse response) {
        var dto = categoryFacade.insert(categoryRequest);
        URI uri = getURIFor(dto.getCode());
        response.addHeader(HttpHeaders.LOCATION, uri.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable(name = "id") String code,
                                                   @Valid @RequestBody CategoryRequest categoryRequest) {
        var response = categoryFacade.update(code, categoryRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByCode(@PathVariable(name = "id") String code) {
        categoryFacade.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }

    private URI getURIFor(String code) {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{code}")
                .buildAndExpand(code)
                .toUri();
    }
}

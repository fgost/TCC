package com.example.application.backend.component;

import com.example.application.backend.component.domain.ComponentEntity;
import com.example.application.backend.component.model.ComponentRequest;
import com.example.application.backend.component.model.ComponentResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/carview/v1/component")
@Validated
public class ComponentController {
    private ComponentFacade componentFacade;

    @GetMapping
    public ResponseEntity<List<ComponentEntity>> findAll() {
        var types = componentFacade.findAll();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponentResponse> findByCode(@PathVariable(name = "id") String code) {
        var response = componentFacade.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ComponentResponse> insert(
            @Valid @RequestBody ComponentRequest componentRequest, HttpServletResponse response) {
        ComponentResponse dto = componentFacade.insert(componentRequest);
        URI uri = getURIFor(dto.getCode());
        response.addHeader(HttpHeaders.LOCATION, uri.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponentResponse> update(@PathVariable(name = "id") String code,
                                                    @Valid @RequestBody ComponentRequest componentRequest) {
        var response = componentFacade.update(code, componentRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByCode(@PathVariable(name = "id") String code) {
        componentFacade.deletedByCode(code);
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

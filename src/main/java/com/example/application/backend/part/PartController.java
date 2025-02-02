package com.example.application.backend.part;

import com.example.application.backend.part.domain.PartEntity;
import com.example.application.backend.part.model.PartRequest;
import com.example.application.backend.part.model.PartResponse;
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
@RequestMapping("/carview/v1/part")
@Validated
public class PartController {
    private PartFacade partFacade;

    @GetMapping
    public ResponseEntity<List<PartEntity >> findAll() {
        var types = partFacade.findAll();
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartResponse> findByCode(@PathVariable(name = "id") String code) {
        var response = partFacade.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PartResponse> insert(
            @Valid @RequestBody PartRequest partRequest, HttpServletResponse response) {
        PartResponse dto = partFacade.insert(partRequest);
        URI uri = getURIFor(dto.getCode());
        response.addHeader(HttpHeaders.LOCATION, uri.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartResponse> update(@PathVariable(name = "id") String code,
                                               @Valid @RequestBody PartRequest partRequest) {
        var response = partFacade.update(code, partRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByCode(@PathVariable(name = "id") String code) {
        partFacade.deletedByCode(code);
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

package com.example.application.backend.maintenancePart;

import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.model.MaintenancePartRequest;
import com.example.application.backend.maintenancePart.model.MaintenancePartResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/carview/v1/maintenance-part")
public class MaintenancePartController {

    private MaintenancePartFacade maintenancePartFacade;

    @GetMapping
    public ResponseEntity<List<MaintenancePartResponse>> findAll() {
        var parts = maintenancePartFacade.findAll();
        return ResponseEntity.ok(parts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenancePartResponse> findByCode(@PathVariable(name = "id") String code) {
        var response = maintenancePartFacade.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MaintenancePartResponse> insert(
            @Valid @RequestBody MaintenancePartEntity request,
            HttpServletResponse response) {
        var dto = maintenancePartFacade.insert(request);
        URI uri = getURIFor(dto.getCode());
        response.addHeader(HttpHeaders.LOCATION, uri.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenancePartResponse> update(@PathVariable(name = "id") String code,
                                                          @Valid @RequestBody MaintenancePartRequest request) {
        var response = maintenancePartFacade.update(code, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByCode(@PathVariable(name = "id") String code) {
        maintenancePartFacade.deletedByCode(code);
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

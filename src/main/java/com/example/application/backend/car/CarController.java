package com.example.application.backend.car;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.model.request.CarRequest;
import com.example.application.backend.car.model.response.CarResponse;
import com.example.application.backend.car.model.response.CarResponseCategory;
import com.example.application.utils.dto.OnlyCodeDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/carview/v1/car")
@Validated
public class CarController {
    private CarFacade carFacade;

    @GetMapping
    public ResponseEntity<List<CarResponse>> findAll(
            @RequestParam(name = "carModel", defaultValue = "") String carModel,
            @RequestParam(name = "year", defaultValue = "") String year) {
        var cars = carFacade.findAll(carModel, year);
        return ResponseEntity.ok(cars);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> findByCode(@PathVariable(name = "id") String code) {
        var response = carFacade.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<CarResponseCategory> getCategories(@PathVariable(name = "id") String code) {
        var response = carFacade.getCategories(code);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CarResponse> insert(
            @Valid @RequestBody CarEntity carRequest, String userDetails,
            HttpServletResponse response) {
        var dto = carFacade.insert(carRequest, userDetails);
        URI uri = getURIFor(dto.getCode());
        response.addHeader(HttpHeaders.LOCATION, uri.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> update(@PathVariable(name = "id") String code,
                                              @Valid @RequestBody CarRequest carRequest) {
        var response = carFacade.update(code, carRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/categories")
    public ResponseEntity<CarResponse> updateCategory(@PathVariable(name = "id") String code,
                                                  @Valid @RequestBody Set<OnlyCodeDto> ids) {
        var response = carFacade.updateCategory(code, ids);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByCode(@PathVariable(name = "id") String code) {
        carFacade.deleteByCode(code);
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

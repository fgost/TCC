package com.example.application.backend.car;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.model.request.CarRequest;
import com.example.application.backend.car.model.response.CarResponse;
import com.example.application.backend.car.model.response.CarResponseCategory;
import com.example.application.utils.dto.OnlyCodeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Set;

/**
 * Controller class for handling HTTP requests related to cars. This class defines various endpoints for performing
 * CRUD operations on car entities and includes methods to retrieve, insert, update, and delete cars.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 *
 * @see CarFacade - Facade layer for car-related operations
 * @see CarResponse - DTO representing general car information
 * @see CarResponseCategory - DTO representing car information with categories
 * @see CarEntity - Entity representing a car
 * @see CarRequest - DTO representing a request for updating car information
 */
@AllArgsConstructor
@RestController
@RequestMapping("/carview/v1/car")
@Validated
@Api(value = "Car", tags = "Car Controller")
@Tag(name = "Car", description = "CRUD of Cars")
public class CarController {

    private CarFacade carFacade;

    @ApiOperation(value = "Get all cars")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return all cars"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping
    public ResponseEntity<List<CarResponse>> findAll(
            @RequestParam(name = "carModel", defaultValue = "") String carModel,
            @RequestParam(name = "year", defaultValue = "") String year) {
        var cars = carFacade.findAll(carModel, year);
        return ResponseEntity.ok(cars);
    }

    @ApiOperation(value = "Get car by External ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car Found", response = CarResponse.class),
            @ApiResponse(code = 400, message = "Bad Request. Check the request parameters."),
            @ApiResponse(code = 401, message = "Unauthorized. Authentication required."),
            @ApiResponse(code = 403, message = "Forbidden. You do not have permission to access this resource."),
            @ApiResponse(code = 404, message = "Not Found. Car or category not found."),
            @ApiResponse(code = 500, message = "Internal Server Error. Something went wrong on the server."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> findByCode(@PathVariable(name = "id") String code) {
        var response = carFacade.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Get categories' car by Car External ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car Found", response = CarResponseCategory.class),
            @ApiResponse(code = 400, message = "Bad Request. Check the request parameters."),
            @ApiResponse(code = 401, message = "Unauthorized. Authentication required."),
            @ApiResponse(code = 403, message = "Forbidden. You do not have permission to access this resource."),
            @ApiResponse(code = 404, message = "Not Found. Car or category not found."),
            @ApiResponse(code = 500, message = "Internal Server Error. Something went wrong on the server."),
    })
    @GetMapping("/{id}/categories")
    public ResponseEntity<CarResponseCategory> getCategories(@PathVariable(name = "id") String code) {
        var response = carFacade.getCategories(code);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Insert a new car")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car Inserted", response = CarResponse.class),
            @ApiResponse(code = 400, message = "Bad Request. Check the request parameters."),
            @ApiResponse(code = 401, message = "Unauthorized. Authentication required."),
            @ApiResponse(code = 403, message = "Forbidden. You do not have permission to access this resource."),
            @ApiResponse(code = 404, message = "Not Found. Car or category not found."),
            @ApiResponse(code = 500, message = "Internal Server Error. Something went wrong on the server."),
    })
    @PostMapping
    public ResponseEntity<CarResponse> insert(
            @Valid @RequestBody CarEntity carRequest, String userDetails,
            HttpServletResponse response) {
        var dto = carFacade.insert(carRequest, userDetails);
        URI uri = getURIFor(dto.getCode());
        response.addHeader(HttpHeaders.LOCATION, uri.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @ApiOperation(value = "Update an existing car")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car Updated", response = CarResponse.class),
            @ApiResponse(code = 400, message = "Bad Request. Check the request parameters."),
            @ApiResponse(code = 401, message = "Unauthorized. Authentication required."),
            @ApiResponse(code = 403, message = "Forbidden. You do not have permission to access this resource."),
            @ApiResponse(code = 404, message = "Not Found. Car or category not found."),
            @ApiResponse(code = 500, message = "Internal Server Error. Something went wrong on the server."),
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> update(@PathVariable(name = "id") String code,
                                              @Valid @RequestBody CarRequest carRequest) {
        var response = carFacade.update(code, carRequest);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Update categories associated with a car")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Categories associated to the car Updated", response = CarResponse.class),
            @ApiResponse(code = 400, message = "Bad Request. Check the request parameters."),
            @ApiResponse(code = 401, message = "Unauthorized. Authentication required."),
            @ApiResponse(code = 403, message = "Forbidden. You do not have permission to access this resource."),
            @ApiResponse(code = 404, message = "Not Found. Car not found."),
            @ApiResponse(code = 500, message = "Internal Server Error. Something went wrong on the server."),
    })
    @PutMapping("/{id}/categories")
    public ResponseEntity<CarResponse> updateCategory(@PathVariable(name = "id") String code,
                                                      @Valid @RequestBody Set<OnlyCodeDto> ids) {
        var response = carFacade.updateCategory(code, ids);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Delete a car")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Car Deleted"),
            @ApiResponse(code = 400, message = "Bad Request. Check the request parameters."),
            @ApiResponse(code = 401, message = "Unauthorized. Authentication required."),
            @ApiResponse(code = 403, message = "Forbidden. You do not have permission to access this resource."),
            @ApiResponse(code = 404, message = "Not Found. Car not found."),
            @ApiResponse(code = 500, message = "Internal Server Error. Something went wrong on the server."),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByCode(@PathVariable(name = "id") String code) {
        carFacade.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }

    /**
     * Generates a URI for the specified resource based on the provided external code (UUID).
     * This method constructs a URI for a resource identified by the external code (UUID) within the
     * current request's URI. The resulting URI can be used, for example, for location headers in HTTP responses.
     *
     * @param code - String representing the external code (UUID) of the resource.
     * @return {@link URI} - A fully qualified URI for the specified resource.
     *
     * @apiNote - The generated URI is constructed by appending the provided code to the current request's URI.
     * For example, if the current request URI is "/cars" and the provided code is "abc123", the
     * generated URI will be "/cars/abc123".
     */
    private URI getURIFor(String code) {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{code}")
                .buildAndExpand(code)
                .toUri();
    }
}

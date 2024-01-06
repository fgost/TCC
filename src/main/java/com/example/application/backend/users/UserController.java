package com.example.application.backend.users;

import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.dto.UserDtoUpdate;
import com.example.application.backend.users.model.request.UserRequest;
import com.example.application.backend.users.model.response.UserResponse;
import com.example.application.backend.users.model.response.UserResponseCar;
import com.example.application.backend.users.model.response.UserResponsePreference;
import com.example.application.backend.users.model.response.UserResponseSummary;
import com.example.application.config.properties.ApplicationProperties;
import com.example.application.utils.dto.OnlyCodeDto;
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

@AllArgsConstructor
@RestController
@RequestMapping("/carview/v1/users")
@Validated
public class UserController {
    private UserFacade usersFacade;
    private ApplicationProperties properties;

    @GetMapping
    public ResponseEntity<List<UserEntity>> findAll(
            @RequestParam(name = "nome", defaultValue = "") String name,
            @RequestParam(name = "email", defaultValue = "") String email) {
        var response = usersFacade.findAll(name, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findByCode(@PathVariable(name = "id") String code) {
        var response = usersFacade.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/preferences")
    public ResponseEntity<UserResponsePreference> getPreferences(@PathVariable(name = "id") String code) {
        var response = usersFacade.getPreferences(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/cars")
    public ResponseEntity<UserResponseCar> getCars(@PathVariable(name = "id") String code) {
        var response = usersFacade.getCars(code);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponseSummary> insert(
            @Valid @RequestBody UserEntity usersRequest,
            HttpServletResponse response) {

        var dto = usersFacade.insert(usersRequest);
        URI uri = getURIFor(dto.getCode());
        response.addHeader(HttpHeaders.LOCATION, uri.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseSummary> update(@PathVariable(name = "id") String code,
                                                      @Valid @RequestBody UserDtoUpdate userDtoUpdate) {
        var response = usersFacade.update(code, userDtoUpdate);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/car")
    public ResponseEntity<UserResponse> updateCar(@PathVariable(name = "id") String code,
                                                  @Valid @RequestBody Set<OnlyCodeDto> ids) {
        var response = usersFacade.updateCar(code, ids);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByCode(@PathVariable(name = "id") String code) {
        usersFacade.deleteByCode(code);
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

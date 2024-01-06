package com.example.application.backend.type;

import com.example.application.backend.type.domain.TypeEntity;
import com.example.application.backend.type.domain.TypeEnum;
import com.example.application.backend.type.model.TypeResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/carview/v1/type")
@Validated
public class TypeController {
    private TypeFacade typeFacade;

    @GetMapping
    public ResponseEntity<List<TypeEntity>> findAll(@RequestParam(name = "typeName", defaultValue = "") TypeEnum typeName) {
        var types = typeFacade.findAll(typeName);
        return ResponseEntity.ok(types);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeResponse> findByCode(@PathVariable(name = "id") String code) {
        var response = typeFacade.findByCode(code);
        return ResponseEntity.ok(response);
    }

//    @PostMapping
//    public ResponseEntity<TypeResponse> insert(
//            @Valid @RequestBody TypeRequest typeRequest,
//            HttpServletResponse response) {
//        var dto = typeFacade.insert(typeRequest);
//        URI uri = getURIFor(dto.getCode());
//        response.addHeader(HttpHeaders.LOCATION, uri.toString());
//        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<TypeResponse> update(@PathVariable(name = "id") String code,
//                                               @Valid @RequestBody TypeRequest typeRequest) {
//        var response = typeFacade.update(code, typeRequest);
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteByCode(@PathVariable(name = "id") String code) {
//        typeFacade.deletedByCode(code);
//        return ResponseEntity.noContent().build();
//    }
//
//    private URI getURIFor(String code) {
//        return ServletUriComponentsBuilder
//                .fromCurrentRequestUri()
//                .path("/{code}")
//                .buildAndExpand(code)
//                .toUri();
//    }
}

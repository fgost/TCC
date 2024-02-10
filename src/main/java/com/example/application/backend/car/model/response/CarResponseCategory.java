package com.example.application.backend.car.model.response;

import com.example.application.backend.car.domain.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Represents the response DTO for a car with associated categories. The CarResponseCategory class encapsulates the
 * response data for a car with associated categories. It provides accessors (getters and setters) for the model name
 * and categories.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 *
 * @see lombok.Getter - Lombok annotation for generating getter methods
 * @see lombok.Setter - Lombok annotation for generating setter methods
 * @see lombok.AllArgsConstructor - Lombok annotation for generating an all-args constructor
 * @see lombok.NoArgsConstructor - Lombok annotation for generating a no-args constructor
 * @see JsonProperty - Jackson annotation to specify the JSON property name
 * @see Category - Class representing a category associated with a car
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarResponseCategory {

    @JsonProperty(value = "carName")
    private String carModel;

    @JsonProperty(value = "categories")
    private Set<Category> categories;
}

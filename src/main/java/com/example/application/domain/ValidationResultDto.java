package com.example.application.domain;

import lombok.Builder;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

@Builder
@Getter
public class ValidationResultDto {
    private String errorMessage;

    private boolean valid;

    public void makeInvalid(String errorMessage){
        this.errorMessage = errorMessage;
        this.valid = false;
    }

    public void makeValid(){
        this.errorMessage = Strings.EMPTY;
        this.valid = true;
    }
}

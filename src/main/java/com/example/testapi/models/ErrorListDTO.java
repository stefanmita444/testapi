package com.example.testapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorListDTO {

    private List<Error> errors;

}

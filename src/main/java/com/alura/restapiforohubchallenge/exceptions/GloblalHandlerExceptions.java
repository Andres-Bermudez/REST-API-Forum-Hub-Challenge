package com.alura.restapiforohubchallenge.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GloblalHandlerExceptions {

    // Cuando el usuario ingrese informacion no valida que no pase las verificaciones de los
    // validadores de las reglas de negocio, retorne un 400 Bad Request y le informe cual es el error.
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handlingValidationsExceptions(ValidationException e) {
        return  ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}

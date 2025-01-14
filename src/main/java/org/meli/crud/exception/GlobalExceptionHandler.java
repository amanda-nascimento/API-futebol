package org.meli.crud.exception;

import org.meli.crud.exception.ConflitosDadosException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Captura a exceção personalizada ConflitoDadosException
    @ExceptionHandler(ConflitosDadosException.class)
    public ResponseEntity<String> handleConflitoDeDados(ConflitosDadosException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    // Captura a exceção personalizada ErrosDeBaseDeDados
    @ExceptionHandler(ErrosDeBaseDeDados.class)
    public ResponseEntity<String> handleConflitoDeDados(ErrosDeBaseDeDados ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // captura outras exceções genéricas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno no servidor: " + ex.getMessage());
    }
}

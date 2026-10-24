package com.example.helpdesk.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class globalExceptionclass {

    @ExceptionHandler(DuplicateEmail.class)
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmail ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler(ResourseNotfoundException.class)
    public ResponseEntity<String> handlerresoursenotfound(ResourseNotfoundException ex){
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(400).body(errors);
    }
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<String> handleInvalidOperation(InvalidOperationException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}

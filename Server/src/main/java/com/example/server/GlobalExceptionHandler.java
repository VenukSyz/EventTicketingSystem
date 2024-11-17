package com.example.server;

import com.example.server.exceptions.DuplicateFieldException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDatabaseException(DataAccessException ex){
        return new ResponseEntity<>("Database connection error. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleUnexpectedErrors(RuntimeException ex){
        return new ResponseEntity<>("An unexpected error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<String> handleDuplicateFieldException(DuplicateFieldException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}

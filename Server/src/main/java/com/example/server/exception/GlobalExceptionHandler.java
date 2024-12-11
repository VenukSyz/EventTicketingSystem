package com.example.server.exception;

import com.example.server.dto.ApiResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the Spring Boot application.
 * This class is responsible for handling various types of exceptions that might occur in the application.
 * It catches exceptions, logs them, and sends appropriate error responses to the client.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles DataAccessException for database connection errors.
     *
     * @param ex the exception that occurred
     * @return a ResponseEntity with an ApiResponse containing the error message and status code
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<String>> handleDatabaseException(DataAccessException ex){
        return new ResponseEntity<>(new ApiResponse<>("Database connection error. Please try again later."), HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Handles unexpected runtime errors.
     *
     * @param ex the exception that occurred
     * @return a ResponseEntity with an ApiResponse containing the error message and status code
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleUnexpectedErrors(RuntimeException ex){
        return new ResponseEntity<>(new ApiResponse<>("An unexpected error occurred!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles NullPointerException, typically when an object is not properly initialized.
     *
     * @param ex the exception that occurred
     * @return a ResponseEntity with an ApiResponse containing the error message and status code
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<String>> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>(new ApiResponse<>("A required object was not found. Please check the request data."), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException, typically for invalid method arguments.
     *
     * @param ex the exception that occurred
     * @return a ResponseEntity with an ApiResponse containing the error message and status code
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ApiResponse<>("Invalid input. Please check the provided arguments."), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation errors for request bodies (e.g., when validation annotations fail).
     *
     * @param ex the exception that occurred
     * @return a ResponseEntity with an ApiResponse containing the error message and status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(new ApiResponse<>(errorMessage), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ConstraintViolationException for validation errors on individual fields.
     *
     * @param ex the exception that occurred
     * @return a ResponseEntity with an appropriate error message and status code
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(new ApiResponse<>("Validation error: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * A fallback handler for any unhandled exceptions. This ensures that the client always receives a response.
     *
     * @param ex the exception that occurred
     * @return a ResponseEntity with an appropriate error message and status code
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>("An error occurred. Please try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

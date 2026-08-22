package com.jobtrack.jobtrack.exception;

import com.jobtrack.jobtrack.dto.ApiErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleValidationErrors(
                        MethodArgumentNotValidException exception) {
                Map<String, String> errors = new LinkedHashMap<>();

                exception.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> errors.putIfAbsent(
                                                error.getField(),
                                                error.getDefaultMessage()));

                ApiErrorResponse response = new ApiErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation failed",
                                errors);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        @ExceptionHandler(EmailAlreadyExistsException.class)
        public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(
                        EmailAlreadyExistsException exception) {
                ApiErrorResponse response = new ApiErrorResponse(
                                HttpStatus.CONFLICT.value(),
                                exception.getMessage(),
                                Map.of());

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(response);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiErrorResponse> handleUnreadableMessage(
                        HttpMessageNotReadableException exception) {
                ApiErrorResponse response = new ApiErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "Malformed or unreadable JSON request",
                                Map.of());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        @ExceptionHandler(JobApplicationNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleJobApplicationNotFound(
                        JobApplicationNotFoundException exception) {
                ApiErrorResponse response = new ApiErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                exception.getMessage(),
                                Map.of());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(response);
        }
}
package com.Maksym.Savienkov.userstestassignmentapp.controller;

import com.Maksym.Savienkov.userstestassignmentapp.exception.ApiError;
import com.Maksym.Savienkov.userstestassignmentapp.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class CommonErrorHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, List<ApiError>>> handleException(Exception ex) {
        log.error("Something went wrong: ", ex);
        Map<String, List<ApiError>> response = new HashMap<>();
        response.put("errors", List.of(
                        new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                ex.getMessage()
                        )
                )
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, List<ApiError>>> handleInvalidRequestException(InvalidRequestException ex) {
        log.error("Failed to process request due to exception", ex);
        Map<String, List<ApiError>> response = new HashMap<>();
        response.put("errors", List.of(
                    new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()
                    )
                )
        );
        return ResponseEntity.badRequest().body(response);
    }
}

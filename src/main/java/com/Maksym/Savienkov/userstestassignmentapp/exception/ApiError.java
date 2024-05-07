package com.Maksym.Savienkov.userstestassignmentapp.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
public class ApiError {
    private Integer status;
    private String message;
    private Date timestamp;

    public ApiError(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}

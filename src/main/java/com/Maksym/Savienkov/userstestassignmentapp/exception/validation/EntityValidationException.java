package com.Maksym.Savienkov.userstestassignmentapp.exception.validation;

import com.Maksym.Savienkov.userstestassignmentapp.exception.AppException;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityValidationException extends AppException {
    protected final List<String> messages;

    public EntityValidationException() {
        super("Validation exception.");
        messages = new ArrayList<>();
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    @Override
    public String getMessage() {
        return String.join(" ", messages);
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }
}

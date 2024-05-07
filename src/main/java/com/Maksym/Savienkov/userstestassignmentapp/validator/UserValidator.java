package com.Maksym.Savienkov.userstestassignmentapp.validator;

import com.Maksym.Savienkov.userstestassignmentapp.dto.UserDto;
import com.Maksym.Savienkov.userstestassignmentapp.exception.validation.UserValidationException;
import com.Maksym.Savienkov.userstestassignmentapp.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class UserValidator {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+){0,10}@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+){0,10}(\\.[A-Za-z]{2,})$";
    private static final String PHONE_NUMBER_PATTERN = "^[\\+]?[0-9]{0,2}[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

    private final Integer userValidAge;
    private final UserValidationException exception;

    public UserValidator(@Value("${user.valid.age}") Integer userValidAge) {
        this.exception = new UserValidationException();
        this.userValidAge = userValidAge;
    }

    public void validate(User user) {
        if (Objects.isNull(user)) {
            exception.addMessage("User cannot be null.");
        } else {
            if (Objects.nonNull(user.getEmail())) {
                validateEmail(user.getEmail());
            } else {
                exception.addMessage("Email cannot be null.");
            }

            if (Objects.nonNull(user.getFirstName())) {
                validateFirstName(user.getFirstName());
            } else {
                exception.addMessage("First name cannot be null.");
            }

            if (Objects.nonNull(user.getLastName())) {
                validateLastName(user.getLastName());
            } else {
                exception.addMessage("Last name cannot be null.");
            }

            if (Objects.nonNull(user.getBirthdate())) {
                validateBirthdate(user.getBirthdate());
            } else {
                exception.addMessage("Birthdate cannot be null.");
            }
            if (Objects.nonNull(user.getPhoneNumber())) {
                validatePhoneNumber(user.getPhoneNumber());
            }

        }

        if (!exception.isEmpty()) {
            throw exception;
        }
    }

    public void validatePatch(UserDto userDto) {
        if (Objects.nonNull(userDto.getEmail())) {
            validateEmail(userDto.getEmail());
        }
        if (Objects.nonNull(userDto.getFirstName())) {
            validateFirstName(userDto.getFirstName());
        }
        if (Objects.nonNull(userDto.getLastName())) {
            validateLastName(userDto.getLastName());
        }
        if (Objects.nonNull(userDto.getBirthdate())) {
            validateBirthdate(userDto.getBirthdate());
        }
        if (Objects.nonNull(userDto.getPhoneNumber())) {
            validatePhoneNumber(userDto.getPhoneNumber());
        }

        if (!exception.isEmpty()) {
            throw exception;
        }
    }

    private void validateEmail(String email) {
        if (email.isEmpty()) {
            exception.addMessage("User email cannot be empty.");
        }
        if (email.contains(" ")) {
            exception.addMessage("User email cannot contain spaces.");
        }
        if (email.contains("_")
                || email.contains("!")
                || email.contains("#")
                || email.contains("$")
                || email.contains("%")
                || email.contains("&")
                || email.contains("'")
                || email.contains("*")
                || email.contains("+")
                || email.contains("/")
                || email.contains("=")
                || email.contains("?")
                || email.contains("`")
                || email.contains("{")
                || email.contains("|")
                || email.contains("}")
                || email.contains("~")
                || email.contains("^")
                || email.contains("-")
                || email.contains("]")
                || email.contains("[")) {
            exception.addMessage("User email cannot contain special characters such as (){}`_/\\]['\".");
        }
        if (!email.matches(EMAIL_PATTERN)) {
            exception.addMessage("User email format is invalid.");
        }
    }

    private void validateFirstName(String firstName) {
        if (firstName.isEmpty()) {
            exception.addMessage("User first name cannot be empty.");
        }
        if (firstName.contains(" ")) {
            exception.addMessage("User first name cannot contain spaces.");
        }
    }

    private void validateLastName(String lastName) {
        if (lastName.isEmpty()) {
            exception.addMessage("User last name cannot be empty.");
        }
        if (lastName.contains(" ")) {
            exception.addMessage("User last name cannot contain spaces.");
        }
    }

    private void validateBirthdate(LocalDate birthdate) {
        if (birthdate.isAfter(LocalDate.now())) {
            exception.addMessage("Birthdate must be earlier than current date.");
        } else if (birthdate.isAfter(LocalDate.now().minusYears(userValidAge))) {
            exception.addMessage("The user must be of legal age.");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches(PHONE_NUMBER_PATTERN)) {
            exception.addMessage("User phone number format is invalid.");
        }
    }
}

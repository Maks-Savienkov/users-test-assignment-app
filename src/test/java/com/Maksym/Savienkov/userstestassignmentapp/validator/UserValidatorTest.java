package com.Maksym.Savienkov.userstestassignmentapp.validator;

import com.Maksym.Savienkov.userstestassignmentapp.dto.UserDto;
import com.Maksym.Savienkov.userstestassignmentapp.exception.validation.UserValidationException;
import com.Maksym.Savienkov.userstestassignmentapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class UserValidatorTest {
    private UserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserValidator(18);
    }

    @Test
    void validate_shouldNotThrowException_whenUserIsValid() {
        User validUser = createValidUser();

        assertDoesNotThrow(() -> validator.validate(validUser));
    }

    @Test
    void validate_shouldThrowException_whenUserIsNull() {
        String message = assertThrows(
                UserValidationException.class,
                () -> validator.validate(null)
        ).getMessage();

        assertEquals("User cannot be null.", message);
    }

    @Test
    void validate_shouldThrowDetailedException_whenUsersFieldsAreNull() {
        User nullFieldsUser = User.builder()
                .id(null)
                .email(null)
                .firstName(null)
                .lastName(null)
                .birthdate(null)
                .build();

        String message = assertThrows(
                UserValidationException.class,
                () -> validator.validate(nullFieldsUser)
        ).getMessage();

        assertEquals("Email cannot be null. "
                + "First name cannot be null. "
                + "Last name cannot be null. "
                + "Birthdate cannot be null.", message);
    }

    private static Arguments[] provideInvalidEmailData() {
        return new Arguments[]{
                Arguments.of(""),
                Arguments.of("  "),
                Arguments.of("not blank"),
                Arguments.of("invalid email@stub.com"),
                Arguments.of("invalid.emailstub.com"),
                Arguments.of("invalid.email@test,stub"),
                Arguments.of("invalid.email@\"test.stub\""),
                Arguments.of("invalid.email@test .stub"),
                Arguments.of("invalid.email@tes`t.stub"),
                Arguments.of("invalid.email@test$bar"),
                Arguments.of("invalid.email@ foo.bar"),
                Arguments.of("invalid.email@ foo bar "),
                Arguments.of("invalid.email@'foo.bar'"),
                Arguments.of("invalid.email@@@foo.bar"),
                Arguments.of("invalid/email@foo.bar"),
                Arguments.of("[invalid.email@foo.bar]")
        };
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEmailData")
    @DisplayName("Should fail when we try to save user with invalid Email")
    void validate_shouldThrowException_whenWeTryingToSaveUserWithInvalidEmail(String email) {
        User user = createValidUser();
        user.setEmail(email);

        assertThrows(
                UserValidationException.class,
                () -> validator.validate(user)
        );
    }

    private static Arguments[] provideInvalidNameData() {
        return new Arguments[]{
                Arguments.of(""),
                Arguments.of("  "),
                Arguments.of("not blank"),
        };
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNameData")
    @DisplayName("Should fail when we try to save user with invalid Firstname")
    void validate_shouldThrowException_whenWeTryingToSaveUserWithInvalidFirstname(String firstname) {
        User user = createValidUser();
        user.setFirstName(firstname);

        assertThrows(
                UserValidationException.class,
                () -> validator.validate(user)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNameData")
    @DisplayName("Should fail when we try to save user with invalid Lastname")
    void validate_shouldThrowException_whenWeTryingToSaveUserWithInvalidLastname(String lastname) {
        User user = createValidUser();
        user.setLastName(lastname);

        assertThrows(
                UserValidationException.class,
                () -> validator.validate(user)
        );
    }

    private static Arguments[] provideInvalidBirthdateData() {
        return new Arguments[]{
                Arguments.of(LocalDate.now().plusDays(1)),
                Arguments.of(LocalDate.now().minusDays(1))
        };
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBirthdateData")
    @DisplayName("Should fail when we try to save user with invalid Birthdate")
    void validate_shouldThrowException_whenWeTryingToSaveUserWithInvalidBirthdate(LocalDate birthdate) {
        User user = createValidUser();
        user.setBirthdate(birthdate);

        assertThrows(
                UserValidationException.class,
                () -> validator.validate(user)
        );
    }

    private static Arguments[] provideInvalidPhoneNumberData() {
        return new Arguments[]{
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of("234223")
        };
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPhoneNumberData")
    @DisplayName("Should fail when we try to save user with invalid Phone number")
    void validate_shouldThrowException_whenWeTryingToSaveUserWithInvalidPhoneNumber(String phoneNumber) {
        User user = createValidUser();
        user.setPhoneNumber(phoneNumber);

        assertThrows(
                UserValidationException.class,
                () -> validator.validate(user)
        );
    }

    @Test
    void validatePatch_shouldNotThrowException_whenUserDtoIsValid() {
        UserDto validUserDto = createValidUserDto();

        assertDoesNotThrow(() -> validator.validatePatch(validUserDto));
    }

    private static User createValidUser() {
        return User.builder()
                .id(0)
                .email("email.1@gmail.com")
                .firstName("Username1")
                .lastName("Lastname1")
                .birthdate(LocalDate.now().minusYears(26))
                .address("Adders")
                .phoneNumber("+340234212131")
                .build();
    }

    private static UserDto createValidUserDto() {
        return UserDto.builder()
                .email("email.1@gmail.com")
                .firstName("Username1")
                .lastName("Lastname1")
                .birthdate(LocalDate.now().minusYears(26))
                .address("Adders")
                .phoneNumber("+340234212131")
                .build();
    }
}

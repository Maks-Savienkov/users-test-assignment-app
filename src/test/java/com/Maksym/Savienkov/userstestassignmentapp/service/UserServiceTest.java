package com.Maksym.Savienkov.userstestassignmentapp.service;


import com.Maksym.Savienkov.userstestassignmentapp.dto.UserDto;
import com.Maksym.Savienkov.userstestassignmentapp.exception.InvalidRequestException;
import com.Maksym.Savienkov.userstestassignmentapp.exception.validation.UserValidationException;
import com.Maksym.Savienkov.userstestassignmentapp.mapper.impl.UserMapperImpl;
import com.Maksym.Savienkov.userstestassignmentapp.model.User;
import com.Maksym.Savienkov.userstestassignmentapp.repository.RepositoryStub;
import com.Maksym.Savienkov.userstestassignmentapp.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private RepositoryStub repository;

    @Mock
    private UserValidator validator;

    private UserService userService;

    @BeforeEach
    void beforeEachSetUp() {
        userService = new UserService(
                repository,
                new UserMapperImpl(),
                validator
        );
    }

    @Test
    public void create_shouldSaveUserAndReturnUserId_whenUserDtoIsValid() {
        Integer id = assertDoesNotThrow(() -> userService.create(createValidUserDto()));
        assertEquals(0, id);

        verify(validator, times(1)).validate(any());
        verify(repository, times(1)).save(createUserWithoutId());
    }

    @Test
    public void create_shouldThrowException_whenUserDtoIsInvalid() {
        UserValidationException userValidationException = new UserValidationException();
        userValidationException.addMessage("message");

        doThrow(userValidationException)
                .when(validator).validate(any(User.class));

        String message = assertThrows(
                InvalidRequestException.class,
                () -> userService.create(createValidUserDto())
        ).getMessage();
        assertEquals("Failed to create user. message", message);

        verify(repository, never()).save(any());
    }

    @Test
    public void getByBirthdate_shouldGetListOfUsersFilteredByBirthdate_whenFromIsBeforeOrEqualTo() {
        List<User> expectedUsers = List.of(
                User.builder()
                        .id(1)
                        .email("email.1@gmail.com")
                        .firstName("Username1")
                        .lastName("Lastname1")
                        .birthdate(LocalDate.now().minusYears(26))
                        .build(),
                User.builder()
                        .id(4)
                        .email("email.4@gmail.com")
                        .firstName("Username4")
                        .lastName("Lastname4")
                        .birthdate(LocalDate.now().minusYears(18))
                        .phoneNumber("+(380)95-123-32-23")
                        .build()
        );

        when(repository.getAll())
                .thenReturn(
                        List.of(
                                User.builder()
                                        .id(1)
                                        .email("email.1@gmail.com")
                                        .firstName("Username1")
                                        .lastName("Lastname1")
                                        .birthdate(LocalDate.now().minusYears(26))
                                        .build(),
                                User.builder()
                                        .id(2)
                                        .email("email.2@gmail.com")
                                        .firstName("Username2")
                                        .lastName("Lastname2")
                                        .birthdate(LocalDate.now().minusYears(48))
                                        .address("some address 2")
                                        .build(),
                                User.builder()
                                        .id(3)
                                        .email("email.3@gmail.com")
                                        .firstName("Username3")
                                        .lastName("Lastname3")
                                        .birthdate(LocalDate.now().minusYears(34))
                                        .address("some address 3")
                                        .phoneNumber("+(380)96-342-43-54")
                                        .build(),
                                User.builder()
                                        .id(4)
                                        .email("email.4@gmail.com")
                                        .firstName("Username4")
                                        .lastName("Lastname4")
                                        .birthdate(LocalDate.now().minusYears(18))
                                        .phoneNumber("+(380)95-123-32-23")
                                        .build()
                        )
                );

        List<User> actualUsers = userService.getByBirthdate(
                LocalDate.now().minusYears(26),
                LocalDate.now().minusYears(18)
        );

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void getByBirthdate_shouldThrowException_whenFromIsAfterTo() {
        String message = assertThrows(
                InvalidRequestException.class,
                () -> userService.getByBirthdate(
                        LocalDate.now().minusYears(18),
                        LocalDate.now().minusYears(26)
                )
        ).getMessage();
        assertEquals("Failed to get users. 'from' cannot be after 'to'.", message);
        verify(repository, never()).getAll();
    }

    @Test
    void update_shouldUpdateUser_whenUserDtoIsValid() {
        User updatedFirstUser = User.builder()
                .id(0)
                .email("emailDto.1@gmail.com")
                .firstName("UserDtoName1")
                .lastName("LastnameDto1")
                .birthdate(LocalDate.now().minusYears(22))
                .build();
        when(repository.getByid(0))
                .thenReturn(createFirstUser());

        assertDoesNotThrow(() -> userService.update(0, createValidUserDto()));

        verify(validator, times(1)).validate(updatedFirstUser);
        verify(repository, times(1)).save(updatedFirstUser);
    }

    @Test
    public void update_shouldThrowException_whenUserDtoIsInvalid() {
        UserValidationException userValidationException = new UserValidationException();
        userValidationException.addMessage("message");

        when(repository.getByid(any()))
                .thenReturn(new User());
        doThrow(userValidationException)
                .when(validator).validate(any(User.class));

        String message = assertThrows(
                InvalidRequestException.class,
                () -> userService.update(0, createValidUserDto())
        ).getMessage();
        assertEquals("Failed to update user. message", message);
        verify(repository, never()).save(any());
    }

    @Test
    void patch_shouldUpdateUser_whenUserDtoIsValid() {
        User patchedFirstUser = User.builder()
                .id(0)
                .email("emailDto.1@gmail.com")
                .firstName("UserDtoName1")
                .lastName("LastnameDto1")
                .birthdate(LocalDate.now().minusYears(22))
                .build();
        when(repository.getByid(0))
                .thenReturn(createFirstUser());

        assertDoesNotThrow(() -> userService.patch(0, createValidUserDto()));

        verify(validator, times(1))
                .validatePatch(createValidUserDto());
        verify(repository, times(1))
                .save(patchedFirstUser);
    }

    @Test
    public void patch_shouldThrowException_whenUserDtoIsInvalid() {
        UserValidationException userValidationException = new UserValidationException();
        userValidationException.addMessage("message");

        doThrow(userValidationException)
                .when(validator).validatePatch(any(UserDto.class));

        String message = assertThrows(
                InvalidRequestException.class,
                () -> userService.patch(0, createValidUserDto())
        ).getMessage();
        assertEquals("Failed to patch user. message", message);
        verify(repository, never()).save(any());
    }

    @Test
    public void delete_shouldDeleteUser() {
        assertDoesNotThrow(() -> userService.delete(0));
        verify(repository, times(1)).deleteById(0);
    }

    private static UserDto createValidUserDto() {
        return new UserDto(
                "emailDto.1@gmail.com",
                "UserDtoName1",
                "LastnameDto1",
                LocalDate.now().minusYears(22),
                null,
                null
        );
    }

    private static User createUserWithoutId() {
        return new User(
                null,
                "emailDto.1@gmail.com",
                "UserDtoName1",
                "LastnameDto1",
                LocalDate.now().minusYears(22),
                null,
                null
        );
    }

    private static User createFirstUser() {
        return User.builder()
                .id(0)
                .email("email.1@gmail.com")
                .firstName("Username1")
                .lastName("Lastname1")
                .birthdate(LocalDate.now().minusYears(26))
                .build();
    }
}

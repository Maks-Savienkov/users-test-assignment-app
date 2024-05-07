package com.Maksym.Savienkov.userstestassignmentapp.service;

import com.Maksym.Savienkov.userstestassignmentapp.dto.UserDto;
import com.Maksym.Savienkov.userstestassignmentapp.exception.GetByBirthdateRequestBoundsException;
import com.Maksym.Savienkov.userstestassignmentapp.exception.InvalidRequestException;
import com.Maksym.Savienkov.userstestassignmentapp.exception.validation.UserValidationException;
import com.Maksym.Savienkov.userstestassignmentapp.mapper.UserMapper;
import com.Maksym.Savienkov.userstestassignmentapp.model.User;
import com.Maksym.Savienkov.userstestassignmentapp.repository.RepositoryStub;
import com.Maksym.Savienkov.userstestassignmentapp.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final RepositoryStub repository;
    private final UserMapper mapper;
    private final UserValidator validator;

    public Integer create(UserDto userDto) {
        log.info("Try to create new user");
        User user = mapper.toModel(userDto);
        try {
            validator.validate(user);
            Integer id = repository.save(user);
            log.info("User created with id: " + id + ".");
            return id;
        } catch (UserValidationException e) {
            log.error("Failed to create user." + e.getMessage());
            throw new InvalidRequestException("Failed to create user. " + e.getMessage());
        }
    }

    public List<User> getByBirthdate(LocalDate from, LocalDate to) {
        try {
            if (from.isAfter(to)) {
                throw new GetByBirthdateRequestBoundsException("'from' cannot be after 'to'.");
            }
            return repository.getAll().stream()
                    .filter(x -> (x.getBirthdate().isAfter(from) || x.getBirthdate().isEqual(from))
                            && (x.getBirthdate().isBefore(to) || x.getBirthdate().isEqual(to)))
                    .collect(Collectors.toList());

        } catch (GetByBirthdateRequestBoundsException e) {
            log.error("Failed to get users." + e.getMessage());
            throw new InvalidRequestException("Failed to get users. " + e.getMessage());
        }
    }

    public void update(Integer id, UserDto userDto) {
        log.info("Try to update user with id: " + id);

        try {
            User user = repository.getByid(id);
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setBirthdate(userDto.getBirthdate());
            user.setAddress(userDto.getAddress());
            user.setPhoneNumber(userDto.getPhoneNumber());
            validator.validate(user);
            repository.save(user);
            log.info("User with id: " + id + " updated successfully");
        } catch (UserValidationException e) {
            log.error("Failed to update user." + e.getMessage());
            throw new InvalidRequestException("Failed to update user. " + e.getMessage());
        }
    }

    public void patch(Integer id, UserDto userDto) {
        log.info("Try to patch user with id: " + id);

        try {
            validator.validatePatch(userDto);
            User user = repository.getByid(0);

            for (Field field : UserDto.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object userDtoFieldValue = ReflectionUtils.getField(field, userDto);
                field.setAccessible(false);
                if (Objects.nonNull(userDtoFieldValue)) {
                    Field userField = ReflectionUtils.findField(User.class, field.getName());
                    userField.setAccessible(true);
                    ReflectionUtils.setField(userField, user, userDtoFieldValue);
                    userField.setAccessible(false);
                }
            }
            repository.save(user);
            log.info("User with id: " + id + " patched successfully");
        } catch (UserValidationException e) {
            log.error("Failed to patch user." + e.getMessage());
            throw new InvalidRequestException("Failed to patch user. " + e.getMessage());
        }
    }

    public void delete(Integer id) {
        log.info("Try to delete user with id: " + id);
        repository.deleteById(id);
        log.info("User with id: " + id + " no longer exists");
    }
}

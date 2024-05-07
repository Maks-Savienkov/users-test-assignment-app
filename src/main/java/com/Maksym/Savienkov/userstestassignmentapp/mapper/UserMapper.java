package com.Maksym.Savienkov.userstestassignmentapp.mapper;

import com.Maksym.Savienkov.userstestassignmentapp.dto.UserDto;
import com.Maksym.Savienkov.userstestassignmentapp.model.User;

public interface UserMapper {
    UserDto toDTO(User model);

    User toModel(UserDto dto);
}

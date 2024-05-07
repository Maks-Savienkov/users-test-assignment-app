package com.Maksym.Savienkov.userstestassignmentapp.mapper.impl;

import com.Maksym.Savienkov.userstestassignmentapp.dto.UserDto;
import com.Maksym.Savienkov.userstestassignmentapp.mapper.UserMapper;
import com.Maksym.Savienkov.userstestassignmentapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDto toDTO(User model) {
        if (model == null) {
            return null;
        } else {
            return UserDto.builder()
                    .email(model.getEmail())
                    .firstName(model.getFirstName())
                    .lastName(model.getLastName())
                    .birthdate(model.getBirthdate())
                    .address(model.getAddress())
                    .phoneNumber(model.getPhoneNumber())
                    .build();
        }
    }

    @Override
    public User toModel(UserDto dto) {
        if (dto == null) {
            return null;
        } else {
            return User.builder()
                    .email(dto.getEmail())
                    .firstName(dto.getFirstName())
                    .lastName(dto.getLastName())
                    .birthdate(dto.getBirthdate())
                    .address(dto.getAddress())
                    .phoneNumber(dto.getPhoneNumber())
                    .build();
        }
    }
}

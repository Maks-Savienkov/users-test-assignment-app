package com.Maksym.Savienkov.userstestassignmentapp.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String address;
    private String phoneNumber;
}

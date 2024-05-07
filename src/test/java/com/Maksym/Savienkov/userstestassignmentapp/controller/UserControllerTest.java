package com.Maksym.Savienkov.userstestassignmentapp.controller;

import com.Maksym.Savienkov.userstestassignmentapp.dto.UserDto;
import com.Maksym.Savienkov.userstestassignmentapp.exception.InvalidRequestException;
import com.Maksym.Savienkov.userstestassignmentapp.model.User;
import com.Maksym.Savienkov.userstestassignmentapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService service;

    @Test
    public void givenGetByBirthdate_whenGetUsersByBirthdate_thenStatus200() throws Exception {
        when(service.getByBirthdate(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class)
                ))
                .thenReturn(List.of(createValidUser()));

        User userToReturn = createValidUser();
        UserDto userDto = createUserDto();
        userToReturn.removeLinks()
                .add(
                    linkTo(
                            methodOn(UserController.class).update(userToReturn.getId(), userDto)
                    ).withRel("update"))
                .add(
                        linkTo(
                                methodOn(UserController.class).patch(userToReturn.getId(), userDto)
                        ).withRel("patch"))
                .add(
                        linkTo(
                                methodOn(UserController.class).delete(userToReturn.getId())
                        ).withRel("delete"));

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users")
                        .param("from", "1998-01-01")
                        .param("to", "2006-01-01")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("{\"data\":[" + mapper.writeValueAsString(userToReturn) + "]}")
                )
                .andReturn();
    }

    @Test
    public void givenGetByBirthdate_whenFromIsAfterTo_thenStatus400() throws Exception {
        when(service.getByBirthdate(
                Mockito.any(LocalDate.class),
                Mockito.any(LocalDate.class)
        )).thenThrow(new InvalidRequestException("error message"));

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users")
                                .param("from", "2007-01-01")
                                .param("to", "2006-01-01")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[:1].message").value("error message"))
                .andExpect(jsonPath("$.errors[:1].status").value(400))
                .andReturn();
    }

    @Test
    void givenCreate_whenUserDataIsValid_thenStatus201AndReturnLocation() throws Exception {
        when(service.create(createUserDto()))
                .thenReturn(1);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{" +
                                            "\"email\":\"email.1@gmail.com\"," +
                                            "\"firstName\":\"Username1\"," +
                                            "\"lastName\":\"Lastname1\"," +
                                            "\"birthdate\":\" " + LocalDate.now().minusYears(26) + "\"," +
                                            "\"address\":\"Adders\"," +
                                            "\"phoneNumber\":\"+340234212131\"" +
                                        "}"
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/users/1"))
                .andExpect(content().string(""));
    }

    @Test
    void givenUpdate_whenUserAlreadyExists_thenStatus200() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/users/0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{" +
                                        "\"email\":\"email.1@gmail.com\"," +
                                        "\"firstName\":\"Username1\"," +
                                        "\"lastName\":\"Lastname1\"," +
                                        "\"birthdate\":\" " + LocalDate.now().minusYears(26) + "\"," +
                                        "\"address\":\"Adders\"," +
                                        "\"phoneNumber\":\"+340234212131\"" +
                                        "}"
                                )
                )
                .andExpect(status().isOk());

        verify(service, times(1)).update(0, createUserDto());
    }

    @Test
    void givenUpdate_whenUserDoesNotExist_thenStatus201AndReturnLocation() throws Exception {
        when(service.create(createUserDto()))
                .thenReturn(12);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/users/12")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{" +
                                        "\"email\":\"email.1@gmail.com\"," +
                                        "\"firstName\":\"Username1\"," +
                                        "\"lastName\":\"Lastname1\"," +
                                        "\"birthdate\":\" " + LocalDate.now().minusYears(26) + "\"," +
                                        "\"address\":\"Adders\"," +
                                        "\"phoneNumber\":\"+340234212131\"" +
                                        "}"
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/users/12"))
                .andExpect(content().string(""));
    }

    @Test
    void givenPatch_whenUserExists_thenStatus200() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/users/0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{" +
                                        "\"email\":\"email.1@gmail.com\"," +
                                        "\"firstName\":\"Username1\"," +
                                        "\"lastName\":\"Lastname1\"," +
                                        "\"birthdate\":\" " + LocalDate.now().minusYears(26) + "\"," +
                                        "\"address\":\"Adders\"," +
                                        "\"phoneNumber\":\"+340234212131\"" +
                                        "}"
                                )
                )
                .andExpect(status().isOk());

        verify(service, times(1)).patch(0, createUserDto());
    }

    @Test
    void givenPatch_whenUserDoesNotExist_thenStatus204() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/users/12")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{" +
                                        "\"email\":\"email.1@gmail.com\"," +
                                        "\"firstName\":\"Username1\"," +
                                        "\"lastName\":\"Lastname1\"," +
                                        "\"birthdate\":\" " + LocalDate.now().minusYears(26) + "\"," +
                                        "\"address\":\"Adders\"," +
                                        "\"phoneNumber\":\"+340234212131\"" +
                                        "}"
                                )
                )
                .andExpect(status().isNoContent());

        verify(service, times(0)).patch(12, createUserDto());
    }

    @Test
    void givenDelete_thenStatus204() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/12"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(12);
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

    private static UserDto createUserDto() {
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

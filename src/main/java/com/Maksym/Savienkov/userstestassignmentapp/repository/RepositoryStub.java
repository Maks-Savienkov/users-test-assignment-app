package com.Maksym.Savienkov.userstestassignmentapp.repository;

import com.Maksym.Savienkov.userstestassignmentapp.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class RepositoryStub {
    private List<User> users = List.of(
            User.builder()
                    .id(0)
                    .email("email.1@gmail.com")
                    .firstName("Username1")
                    .lastName("Lastname1")
                    .birthdate(LocalDate.now().minusYears(26))
                    .build(),
            User.builder()
                    .id(1)
                    .email("email.2@gmail.com")
                    .firstName("Username2")
                    .lastName("Lastname2")
                    .birthdate(LocalDate.now().minusYears(48))
                    .address("some address 2")
                    .build(),
            User.builder()
                    .id(2)
                    .email("email.3@gmail.com")
                    .firstName("Username3")
                    .lastName("Lastname3")
                    .birthdate(LocalDate.now().minusYears(34))
                    .address("some address 3")
                    .phoneNumber("+(380)96-342-43-54")
                    .build(),
            User.builder()
                    .id(3)
                    .email("email.4@gmail.com")
                    .firstName("Username4")
                    .lastName("Lastname4")
                    .birthdate(LocalDate.now().minusYears(18))
                    .phoneNumber("+(380)95-123-32-23")
                    .build()
    );
    private int counter;

    public int save(User user) {
        return counter++;
    }

    public List<User> getAll() {
        return users;
    }

    public User getByid(Integer id) {
        return users.get(id);
    }

    public void deleteById(Integer id) { }
}

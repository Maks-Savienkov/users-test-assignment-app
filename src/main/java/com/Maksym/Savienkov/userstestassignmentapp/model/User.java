package com.Maksym.Savienkov.userstestassignmentapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends RepresentationModel<User> {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String address;
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(birthdate, user.birthdate) &&
                Objects.equals(address, user.address) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(getLinks(), user.getLinks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, birthdate, address, phoneNumber, getLinks());
    }
}

package com.Maksym.Savienkov.userstestassignmentapp.controller;

import com.Maksym.Savienkov.userstestassignmentapp.dto.UserDto;
import com.Maksym.Savienkov.userstestassignmentapp.mapper.UserMapper;
import com.Maksym.Savienkov.userstestassignmentapp.model.User;
import com.Maksym.Savienkov.userstestassignmentapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody UserDto userDto) {
        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/api/users/{id}")
                                .buildAndExpand(service.create(userDto))
                                .toUri()
                )
                .build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<User>>> getByBirthdate(
            @RequestParam(value = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(value = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<User> users = service.getByBirthdate(from, to);
        for (User user : users) {
            user.removeLinks();
            UserDto userDto = mapper.toDTO(user);
            user.add(
                    linkTo(
                            methodOn(UserController.class).update(user.getId(), userDto)
                    ).withRel("update")
            ).add(
                    linkTo(
                            methodOn(UserController.class).patch(user.getId(), userDto)
                    ).withRel("patch")
            ).add(
                    linkTo(
                            methodOn(UserController.class).delete(user.getId())
                    ).withRel("delete")
            );
        }
        Map<String, List<User>> response = new HashMap<>();
        response.put("data", users);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Integer id,
            @RequestBody UserDto userDto
    ) {
        // Mocking no existing users
        // if(service.existsById(id)) {
        if (id != 12 && id != 28) {
            service.update(id, userDto);
            return ResponseEntity.ok().build();
        } else {
            log.info("User with id: " + id + " not found. Creating...");
            return create(userDto);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patch(
            @PathVariable Integer id,
            @RequestBody UserDto userDto
    ) {
        // Mocking no existing users
        // if(service.existsById(id)) {
        if (id != 12 && id != 28) {
            service.patch(id, userDto);
            return ResponseEntity.ok().build();
        } else {
            log.info("User with id: " + id + " not found.");
            return ResponseEntity.noContent().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Integer id
    ) {
        service.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}

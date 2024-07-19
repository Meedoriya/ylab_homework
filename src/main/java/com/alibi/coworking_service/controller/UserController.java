package com.alibi.coworking_service.controller;


import com.alibi.coworking_service.domain.dto.UserDto;
import com.alibi.coworking_service.domain.dto.UserRegistrationDto;
import com.alibi.coworking_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user")
    public ResponseEntity<Void> registerUser(@RequestBody UserRegistrationDto userRegistrationDtoDto) {
        userService.registerUser(userRegistrationDtoDto);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Logs in a user")
    public ResponseEntity<UserDto> loginUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        UserDto userDto = userService.loginUser(userRegistrationDto.getUsername(), userRegistrationDto.getPassword());

        return ResponseEntity.ok(userDto);
    }
}

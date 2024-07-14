package org.alibi.controller;

import io.swagger.annotations.ApiOperation;
import org.alibi.domain.dto.UserDto;
import org.alibi.domain.dto.UserRegistrationDto;
import org.alibi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Регистрирует нового пользователя.
     *
     * @param userRegistrationDto DTO регистрации пользователя
     * @return ResponseEntity с статусом 201
     */
    @PostMapping("/register")
    @ApiOperation(value = "Register a new user", notes = "Registers a new user")
    public ResponseEntity<Void> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        userService.registerUser(userRegistrationDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Авторизует пользователя.
     *
     * @param userRegistrationDto DTO авторизации пользователя
     * @return ResponseEntity с DTO пользователя
     */
    @PostMapping("/login")
    @ApiOperation(value = "Login user", notes = "Logs in a user")
    public ResponseEntity<UserDto> loginUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        UserDto userDto = userService.loginUser(userRegistrationDto.getUsername(), userRegistrationDto.getPassword());
        return ResponseEntity.ok(userDto);
    }
}

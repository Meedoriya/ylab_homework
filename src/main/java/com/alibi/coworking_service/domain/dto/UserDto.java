package com.alibi.coworking_service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;


/**
 * DTO для представления пользователя.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotNull
    Long id;

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50)
    String username;
}

package com.alibi.coworking_service.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;



/**
 * DTO для регистрации пользователя.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserRegistrationDto {

    @NotNull(message = "Имя пользователя не должно быть пустым")
    @Size(min = 3, max = 50, message = "Размер имени пользователя должен быть от 3 до 50 символов")
    String username;

    @NotNull(message = "Пароль не должен быть пустым")
    @Size(min = 6, max = 100, message = "Размер пароля должен быть от 6 до 100 символов")
    String password;
}
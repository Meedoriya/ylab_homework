package org.alibi.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO для регистрации пользователя.
 *
 * @param username имя пользователя, не должно быть пустым и должно быть от 3 до 50 символов
 * @param password пароль, не должен быть пустым и должен быть от 6 до 100 символов
 */
public record UserRegistrationDto(
        @NotNull(message = "Имя пользователя не должно быть пустым")
        @Size(min = 3, max = 50, message = "Размер имени пользователя должен быть от 3 до 50 символов")
        String username,
        @NotNull(message = "Пароль не должен быть пустым")
        @Size(min = 6, max = 100, message = "Размер пароля должен быть от 6 до 100 символов")
        String password
) {}

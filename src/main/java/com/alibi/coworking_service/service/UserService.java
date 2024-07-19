package com.alibi.coworking_service.service;

import com.alibi.coworking_service.domain.dto.UserDto;
import com.alibi.coworking_service.domain.dto.UserRegistrationDto;

import java.util.List;

/**
 * Интерфейс сервиса для управления пользователями.
 */
public interface UserService {
    /**
     * Регистрирует нового пользователя.
     * @param userRegistrationDto данные для регистрации пользователя
     */
    void registerUser(UserRegistrationDto userRegistrationDto);

    /**
     * Выполняет вход пользователя.
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return данные о пользователе
     */
    UserDto loginUser(String username, String password);

    /**
     * Возвращает пользователя по его ID.
     * @param id ID пользователя
     * @return данные о пользователе
     */
    UserDto getUserById(Long id);

    /**
     * Возвращает всех пользователей.
     * @return список всех пользователей
     */
    List<UserDto> getAllUsers();
}

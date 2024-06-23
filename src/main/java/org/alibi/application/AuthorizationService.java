package org.alibi.application;

import org.alibi.domain.model.Role;
import org.alibi.domain.model.User;

/**
 * Сервис для проверки ролей пользователей.
 */
public class AuthorizationService {

    /**
     * Проверяет, является ли пользователь администратором.
     *
     * @param user Пользователь для проверки.
     * @return true, если пользователь является администратором, иначе false.
     */
    public boolean isAdmin(User user) {
        return user != null && user.getRole() == Role.ADMIN;
    }

    /**
     * Проверяет, является ли пользователь обычным пользователем.
     *
     * @param user Пользователь для проверки.
     * @return true, если пользователь является обычным пользователем, иначе false.
     */
    public boolean isUser(User user) {
        return user != null && user.getRole() == Role.USER;
    }
}

package org.alibi.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Класс, представляющий пользователя.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    Long id;

    /**
     * Имя пользователя.
     */
    String username;

    /**
     * Пароль пользователя.
     */
    String password;

    /**
     * Роль пользователя.
     */
    Role role;
}

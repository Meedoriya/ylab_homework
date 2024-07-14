package org.alibi.repository;

import org.alibi.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для управления пользователями.
 */
public interface UserRepository {
    /**
     * Сохраняет нового пользователя.
     * @param user пользователь для сохранения
     */
    void save(User user);

    /**
     * Ищет пользователя по его ID.
     * @param id ID пользователя
     * @return Optional с найденным пользователем, если он существует
     */
    Optional<User> findById(Long id);

    /**
     * Ищет пользователя по его имени пользователя.
     * @param username имя пользователя
     * @return Optional с найденным пользователем, если он существует
     */
    Optional<User> findByUsername(String username);

    /**
     * Возвращает всех пользователей.
     * @return список всех пользователей
     */
    List<User> findAll();

    /**
     * Обновляет существующего пользователя.
     * @param user пользователь для обновления
     */
    void update(User user);

    /**
     * Удаляет пользователя по его ID.
     * @param id ID пользователя
     */
    void delete(Long id);

    /**
     * Удаляет всех пользователей.
     */
    void deleteAll();
}

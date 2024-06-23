package org.alibi.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.alibi.domain.model.User;
import org.alibi.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для хранения пользователей в памяти.
 */
@Getter
@Setter
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private Long counter = 1L;

    /**
     * Сохраняет нового пользователя.
     *
     * @param user Пользователь для сохранения.
     */
    @Override
    public void save(User user) {
        user.setId(counter++);
        users.add(user);
    }

    /**
     * Находит пользователя по его ID.
     *
     * @param id ID пользователя.
     * @return Опциональный пользователь.
     */
    @Override
    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id)).findFirst();
    }

    /**
     * Находит пользователя по его имени пользователя.
     *
     * @param username Имя пользователя.
     * @return Опциональный пользователь.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username)).findFirst();
    }

    /**
     * Возвращает всех пользователей.
     *
     * @return Список всех пользователей.
     */
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    /**
     * Обновляет существующего пользователя.
     *
     * @param user Пользователь для обновления.
     */
    @Override
    public void update(User user) {
        delete(user.getId());
        users.add(user);
    }

    /**
     * Удаляет пользователя по его ID.
     *
     * @param id ID пользователя для удаления.
     */
    @Override
    public void delete(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}


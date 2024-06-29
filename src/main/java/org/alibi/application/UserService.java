package org.alibi.application;

import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.User;
import org.alibi.domain.repository.UserRepository;

import java.util.Optional;

/**
 * Сервис для управления пользователями.
 */
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * Регистрирует нового пользователя.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @throws IllegalArgumentException если пользователь с таким именем уже существует.
     */
    public void registerUser(String username, String password) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        userRepository.save(user);
    }

    /**
     * Авторизует пользователя по имени и паролю.
     *
     * @param username Имя пользователя.
     * @param password Пароль пользователя.
     * @return Авторизованный пользователь.
     * @throws IllegalArgumentException если имя пользователя или пароль неверны.
     */
    public User loginUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get();
        } else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }
}

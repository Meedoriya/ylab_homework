package org.alibi.application;

import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.User;
import org.alibi.domain.repository.UserRepository;
import org.alibi.dto.UserDto;
import org.alibi.dto.UserRegistrationDto;
import org.alibi.mapper.UserMapper;

import java.util.Optional;

/**
 * Сервис для управления пользователями.
 */
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;


    /**
     * Регистрирует нового пользователя.
     *
     * @param userRegistrationDto Отвечает за имя и пароль пользователя.
     * @throws IllegalArgumentException если пользователь с таким именем уже существует.
     */
    public void registerUser(UserRegistrationDto userRegistrationDto) {
        Optional<User> existingUser = userRepository.findByUsername(userRegistrationDto.username());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = userMapper.userRegistrationDtoToUser(userRegistrationDto);
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
    public Optional<UserDto> loginUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .map(userMapper::toDto)
                .map(Optional::of)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    }

}

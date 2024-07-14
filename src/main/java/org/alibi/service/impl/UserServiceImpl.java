package org.alibi.service.impl;

import org.alibi.domain.dto.UserDto;
import org.alibi.domain.dto.UserRegistrationDto;
import org.alibi.domain.model.User;
import org.alibi.mapper.UserMapper;
import org.alibi.repository.UserRepository;
import org.alibi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Реализация сервиса для управления пользователями.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void registerUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.findByUsername(userRegistrationDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = userMapper.userRegistrationDtoToUser(userRegistrationDto);
        userRepository.save(user);
    }

    @Override
    public UserDto loginUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return userMapper.toDto(user.get());
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}

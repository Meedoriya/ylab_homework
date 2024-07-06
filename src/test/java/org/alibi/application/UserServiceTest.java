package org.alibi.application;

import org.alibi.domain.model.User;
import org.alibi.domain.repository.UserRepository;
import org.alibi.dto.UserDto;
import org.alibi.dto.UserRegistrationDto;
import org.alibi.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserMapper userMapper = UserMapper.INSTANCE;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should register user successfully")
    void registerUser() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("testUser", "password");

        when(userRepository.findByUsername(userRegistrationDto.username())).thenReturn(Optional.empty());

        userService.registerUser(userRegistrationDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when user already exists")
    void registerUser_UserAlreadyExists() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("testUser", "password");
        when(userRepository.findByUsername(userRegistrationDto.username())).thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(userRegistrationDto));
        assertThat(exception.getMessage()).isEqualTo("User already exists");
    }

    @Test
    @DisplayName("Should login user successfully")
    void loginUser() {
        String username = "testUser";
        String password = "password";
        User user = new User(1L, username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<UserDto> loggedInUser = userService.loginUser(username, password);

        assertThat(loggedInUser).isPresent();
        assertThat(loggedInUser.get().getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("Should throw exception when username or password is invalid")
    void loginUser_InvalidCredentials() {
        String username = "testUser";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.loginUser(username, password));
        assertThat(exception.getMessage()).isEqualTo("Invalid username or password");
    }
}

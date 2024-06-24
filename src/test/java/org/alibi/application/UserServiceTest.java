package org.alibi.application;

import org.alibi.domain.model.Role;
import org.alibi.domain.model.User;
import org.alibi.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should register user")
    void registerUserShouldRegisterUser() {
        String username = "user";
        String password = "password";
        Role role = Role.USER;

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        userService.registerUser(username, password, role);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when user already exists")
    void registerUserShouldThrowExceptionWhenUserAlreadyExists() {
        String username = "user";
        String password = "password";
        Role role = Role.USER;

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerUser(username, password, role))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User already exists");
    }

    @Test
    @DisplayName("Should return user when credentials are valid")
    void loginUserShouldReturnUserWhenCredentialsAreValid() {
        String username = "user";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.loginUser(username, password);

        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("Should throw exception when credentials are invalid")
    void loginUserShouldThrowExceptionWhenCredentialsAreInvalid() {
        String username = "user";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loginUser(username, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid username or password");
    }
}


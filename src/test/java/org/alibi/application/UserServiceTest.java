package org.alibi.application;

import org.alibi.domain.model.User;
import org.alibi.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should register new user")
    void shouldRegisterNewUser() {
        String username = "testUser";
        String password = "testPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        userService.registerUser(username, password);

        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Should throw exception if user already exists")
    void shouldThrowExceptionIfUserAlreadyExists() {
        String username = "testUser";
        String password = "testPassword";
        User existingUser = new User(1L, username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> userService.registerUser(username, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User already exists");
    }

    @Test
    @DisplayName("Should login user with correct credentials")
    void shouldLoginUserWithCorrectCredentials() {
        String username = "testUser";
        String password = "testPassword";
        User user = new User(1L, username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User loggedInUser = userService.loginUser(username, password);

        assertThat(loggedInUser).isEqualTo(user);
    }

    @Test
    @DisplayName("Should throw exception if username or password is incorrect")
    void shouldThrowExceptionIfUsernameOrPasswordIsIncorrect() {
        String username = "testUser";
        String password = "testPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loginUser(username, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid username or password");
    }
}

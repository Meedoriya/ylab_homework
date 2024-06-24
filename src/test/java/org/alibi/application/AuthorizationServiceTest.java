package org.alibi.application;

import org.alibi.domain.model.Role;
import org.alibi.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationServiceTest {

    private final AuthorizationService authorizationService = new AuthorizationService();

    @Test
    @DisplayName("Should return true when user is admin")
    void isAdminShouldReturnTrueWhenUserIsAdmin() {
        User user = new User();
        user.setRole(Role.ADMIN);

        boolean result = authorizationService.isAdmin(user);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when user is not admin")
    void isAdminShouldReturnFalseWhenUserIsNotAdmin() {
        User user = new User();
        user.setRole(Role.USER);

        boolean result = authorizationService.isAdmin(user);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return true when user is user")
    void isUserShouldReturnTrueWhenUserIsUser() {
        User user = new User();
        user.setRole(Role.USER);

        boolean result = authorizationService.isUser(user);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when user is not user")
    void isUserShouldReturnFalseWhenUserIsNotUser() {
        User user = new User();
        user.setRole(Role.ADMIN);

        boolean result = authorizationService.isUser(user);

        assertThat(result).isFalse();
    }
}

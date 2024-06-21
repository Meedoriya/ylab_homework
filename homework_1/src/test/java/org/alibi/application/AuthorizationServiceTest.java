package org.alibi.application;

import org.alibi.domain.model.Role;
import org.alibi.domain.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationServiceTest {

    private final AuthorizationService authorizationService = new AuthorizationService();

    @Test
    void isAdminShouldReturnTrueWhenUserIsAdmin() {
        User user = new User();
        user.setRole(Role.ADMIN);

        boolean result = authorizationService.isAdmin(user);

        assertThat(result).isTrue();
    }

    @Test
    void isAdminShouldReturnFalseWhenUserIsNotAdmin() {
        User user = new User();
        user.setRole(Role.USER);

        boolean result = authorizationService.isAdmin(user);

        assertThat(result).isFalse();
    }

    @Test
    void isUserShouldReturnTrueWhenUserIsUser() {
        User user = new User();
        user.setRole(Role.USER);

        boolean result = authorizationService.isUser(user);

        assertThat(result).isTrue();
    }

    @Test
    void isUserShouldReturnFalseWhenUserIsNotUser() {
        User user = new User();
        user.setRole(Role.ADMIN);

        boolean result = authorizationService.isUser(user);

        assertThat(result).isFalse();
    }
}

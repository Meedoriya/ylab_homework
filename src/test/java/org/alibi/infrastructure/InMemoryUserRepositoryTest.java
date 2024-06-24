package org.alibi.infrastructure;

import org.alibi.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    @DisplayName("Should add user")
    void saveShouldAddUser() {
        User user = new User();
        user.setUsername("user1");

        userRepository.save(user);

        assertThat(userRepository.getUsers()).hasSize(1);
        assertThat(user.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should return user if exists")
    void findByIdShouldReturnUserIfExists() {
        User user = new User();
        user.setUsername("user1");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("Should return empty if not exists")
    void findByIdShouldReturnEmptyIfNotExists() {
        Optional<User> foundUser = userRepository.findById(1L);

        assertThat(foundUser).isNotPresent();
    }

    @Test
    @DisplayName("Should return user if exists")
    void findByUsernameShouldReturnUserIfExists() {
        User user = new User();
        user.setUsername("user1");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("user1");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("Should return empty if not exists")
    void findByUsernameShouldReturnEmptyIfNotExists() {
        Optional<User> foundUser = userRepository.findByUsername("user1");

        assertThat(foundUser).isNotPresent();
    }

    @Test
    @DisplayName("Should return all users")
    void findAllShouldReturnAllUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("Should modify existing user")
    void updateShouldModifyExistingUser() {
        User user = new User();
        user.setUsername("user1");
        userRepository.save(user);

        user.setUsername("updatedUser");
        userRepository.update(user);

        Optional<User> updatedUser = userRepository.findById(user.getId());

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getUsername()).isEqualTo("updatedUser");
    }

    @Test
    @DisplayName("Should remove user")
    void deleteShouldRemoveUser() {
        User user = new User();
        user.setUsername("user1");
        userRepository.save(user);

        userRepository.delete(user.getId());

        assertThat(userRepository.getUsers()).isEmpty();
    }
}

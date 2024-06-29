package org.alibi.infrastructure;

import org.alibi.domain.model.User;
import org.alibi.domain.repository.UserRepository;
import org.alibi.in.DatabaseInitializer;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class UserRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("postgres")
            .withUsername("alibi")
            .withPassword("alibi");

    private Connection connection;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        DatabaseInitializer.initialize(connection);
        userRepository = new UserRepositoryImpl(connection);
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    @DisplayName("Should save and find user by ID")
    void testSaveAndFindById() {
        User user = new User(null, "user1", "password");
        userRepository.save(user);

        Optional<User> retrievedUser = userRepository.findById(user.getId());
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("Should update user")
    void testUpdate() {
        User user = new User(null, "user1", "password");
        userRepository.save(user);

        user.setUsername("updatedUser");
        userRepository.update(user);

        Optional<User> retrievedUser = userRepository.findById(user.getId());
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getUsername()).isEqualTo("updatedUser");
    }

    @Test
    @DisplayName("Should delete user")
    void testDelete() {
        User user = new User(null, "user1", "password");
        userRepository.save(user);

        userRepository.delete(user.getId());

        Optional<User> retrievedUser = userRepository.findById(user.getId());
        assertThat(retrievedUser).isNotPresent();
    }

    @Test
    @DisplayName("Should find all users")
    void testFindAll() {
        User user1 = new User(null, "user1", "password");
        User user2 = new User(null, "user2", "password");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
        assertThat(users).extracting("username").contains("user1", "user2");
    }

    @Test
    @DisplayName("Should find user by username")
    void testFindByUsername() {
        User user = new User(null, "user1", "password");
        userRepository.save(user);

        Optional<User> retrievedUser = userRepository.findByUsername("user1");
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getUsername()).isEqualTo("user1");
    }

}

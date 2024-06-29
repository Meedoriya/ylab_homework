package org.alibi.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.alibi.domain.model.User;
import org.alibi.domain.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для управления пользователями.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final Connection connection;

    /**
     * Сохраняет пользователя в базе данных.
     *
     * @param user пользователь для сохранения.
     */
    @Override
    public void save(User user) {
        String sql = "INSERT INTO coworking_service.user (username, password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();

            try (ResultSet generateKeys = preparedStatement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    user.setId(generateKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving user", e);
        }
    }

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return найденный пользователь или пустое значение, если пользователь не найден.
     */
    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM coworking_service.user WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding user by ID", e);
        }
        return Optional.empty();
    }

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя.
     * @return найденный пользователь или пустое значение, если пользователь не найден.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM coworking_service.user WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding user by username", e);
        }
        return Optional.empty();
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return список пользователей.
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM coworking_service.user";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding all users", e);
        }
        return users;
    }

    /**
     * Обновляет пользователя в базе данных.
     *
     * @param user обновленный пользователь.
     */
    @Override
    public void update(User user) {
        String sql = "UPDATE coworking_service.user SET username = ?, password = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating user", e);
        }
    }

    /**
     * Удаляет пользователя из базы данных.
     *
     * @param id идентификатор пользователя для удаления.
     */
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM coworking_service.user WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting user", e);
        }
    }

    /**
     * Удаляет всех пользователей из базы данных.
     */
    @Override
    public void deleteAll() {
        String sql = "DELETE FROM coworking_service.user";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting all users", e);
        }
    }
}

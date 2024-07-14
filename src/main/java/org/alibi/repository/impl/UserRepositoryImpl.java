package org.alibi.repository.impl;

import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.User;
import org.alibi.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для управления пользователями.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password")
            );
        }
    };

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, username);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }
}

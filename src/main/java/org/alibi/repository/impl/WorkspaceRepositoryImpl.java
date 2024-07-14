package org.alibi.repository.impl;

import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.Workspace;
import org.alibi.repository.WorkspaceRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для управления рабочими местами.
 */
@Repository
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Workspace> workspaceRowMapper = new RowMapper<Workspace>() {
        @Override
        public Workspace mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Workspace(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getBoolean("available")
            );
        }
    };

    @Override
    public void save(Workspace workspace) {
        String sql = "INSERT INTO workspace (name, available) VALUES (?, ?)";
        jdbcTemplate.update(sql, workspace.getName(), workspace.isAvailable());
    }

    @Override
    public Optional<Workspace> findById(Long id) {
        String sql = "SELECT * FROM workspace WHERE id = ?";
        try {
            Workspace workspace = jdbcTemplate.queryForObject(sql, workspaceRowMapper, id);
            return Optional.ofNullable(workspace);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Workspace> findAll() {
        String sql = "SELECT * FROM workspace";
        return jdbcTemplate.query(sql, workspaceRowMapper);
    }

    @Override
    public void update(Workspace workspace) {
        String sql = "UPDATE workspace SET name = ?, available = ? WHERE id = ?";
        jdbcTemplate.update(sql, workspace.getName(), workspace.isAvailable(), workspace.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM workspace WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM workspace";
        jdbcTemplate.update(sql);
    }
}

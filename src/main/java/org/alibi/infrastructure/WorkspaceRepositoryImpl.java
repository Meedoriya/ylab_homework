package org.alibi.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.WorkspaceRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для управления рабочими местами.
 */
@RequiredArgsConstructor
@Getter
@Setter
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final Connection connection;

    /**
     * Сохраняет рабочее место в базе данных.
     *
     * @param workspace рабочее место для сохранения.
     */
    @Override
    public void save(Workspace workspace) {
        String sql = "INSERT INTO coworking_service.workspace (name, available) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, workspace.getName());
            preparedStatement.setBoolean(2, workspace.isAvailable());
            preparedStatement.executeUpdate();

            try (ResultSet generateKeys = preparedStatement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    workspace.setId(generateKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving workspace", e);
        }
    }

    /**
     * Находит рабочее место по идентификатору.
     *
     * @param id идентификатор рабочего места.
     * @return найденное рабочее место или пустое значение, если рабочее место не найдено.
     */
    @Override
    public Optional<Workspace> findById(Long id) {
        String sql = "SELECT * FROM coworking_service.workspace WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Workspace workspace = new Workspace();
                    workspace.setId(resultSet.getLong("id"));
                    workspace.setName(resultSet.getString("name"));
                    workspace.setAvailable(resultSet.getBoolean("available"));
                    return Optional.of(workspace);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding workspace by ID", e);
        }
        return Optional.empty();
    }

    /**
     * Возвращает список всех рабочих мест.
     *
     * @return список рабочих мест.
     */
    @Override
    public List<Workspace> findAll() {
        List<Workspace> workspaces = new ArrayList<>();
        String sql = "SELECT * FROM coworking_service.workspace";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Workspace workspace = new Workspace();
                workspace.setId(resultSet.getLong("id"));
                workspace.setName(resultSet.getString("name"));
                workspace.setAvailable(resultSet.getBoolean("available"));
                workspaces.add(workspace);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding all workspaces", e);
        }
        return workspaces;
    }

    /**
     * Обновляет рабочее место в базе данных.
     *
     * @param workspace обновленное рабочее место.
     */
    @Override
    public void update(Workspace workspace) {
        String sql = "UPDATE coworking_service.workspace SET name = ?, available = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, workspace.getName());
            preparedStatement.setBoolean(2, workspace.isAvailable());
            preparedStatement.setLong(3, workspace.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating workspace", e);
        }
    }

    /**
     * Удаляет рабочее место из базы данных.
     *
     * @param id идентификатор рабочего места для удаления.
     */
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM coworking_service.workspace WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting workspace", e);
        }
    }
    /**
     * Удаляет все рабочие пространства из базы данных.
     */
    @Override
    public void deleteAll() {
        String sql = "DELETE FROM coworking_service.workspace";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting all workspaces", e);
        }
    }

}

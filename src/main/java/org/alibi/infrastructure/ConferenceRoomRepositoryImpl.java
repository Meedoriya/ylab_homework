package org.alibi.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.repository.ConferenceRoomRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для управления конференц-залами.
 */
@RequiredArgsConstructor
@Getter
@Setter
public class ConferenceRoomRepositoryImpl implements ConferenceRoomRepository {

    private final Connection connection;

    /**
     * Сохраняет конференц-зал в базе данных.
     *
     * @param conferenceRoom конференц-зал для сохранения.
     */
    @Override
    public void save(ConferenceRoom conferenceRoom) {
        String sql = "INSERT INTO coworking_service.conference_room (name, available) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, conferenceRoom.getName());
            preparedStatement.setBoolean(2, conferenceRoom.isAvailable());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    conferenceRoom.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving conference room", e);
        }
    }

    /**
     * Находит конференц-зал по идентификатору.
     *
     * @param id идентификатор конференц-зала.
     * @return найденный конференц-зал или пустое значение, если конференц-зал не найден.
     */
    @Override
    public Optional<ConferenceRoom> findById(Long id) {
        String sql = "SELECT * FROM coworking_service.conference_room WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ConferenceRoom conferenceRoom = new ConferenceRoom();
                    conferenceRoom.setId(resultSet.getLong("id"));
                    conferenceRoom.setName(resultSet.getString("name"));
                    conferenceRoom.setAvailable(resultSet.getBoolean("available"));
                    return Optional.of(conferenceRoom);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding conference room by ID", e);
        }
        return Optional.empty();
    }

    /**
     * Возвращает список всех конференц-залов.
     *
     * @return список конференц-залов.
     */
    @Override
    public List<ConferenceRoom> findAll() {
        List<ConferenceRoom> conferenceRooms = new ArrayList<>();
        String sql = "SELECT * FROM coworking_service.conference_room";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ConferenceRoom conferenceRoom = new ConferenceRoom();
                conferenceRoom.setId(resultSet.getLong("id"));
                conferenceRoom.setName(resultSet.getString("name"));
                conferenceRoom.setAvailable(resultSet.getBoolean("available"));
                conferenceRooms.add(conferenceRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding all conference rooms", e);
        }
        return conferenceRooms;
    }

    /**
     * Обновляет конференц-зал в базе данных.
     *
     * @param conferenceRoom обновленный конференц-зал.
     */
    @Override
    public void update(ConferenceRoom conferenceRoom) {
        String sql = "UPDATE coworking_service.conference_room SET name = ?, available = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, conferenceRoom.getName());
            preparedStatement.setBoolean(2, conferenceRoom.isAvailable());
            preparedStatement.setLong(3, conferenceRoom.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating conference room", e);
        }
    }

    /**
     * Удаляет конференц-зал из базы данных.
     *
     * @param id идентификатор конференц-зала для удаления.
     */
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM coworking_service.conference_room WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting conference room", e);
        }
    }

    /**
     * Удаляет все конференц комнаты из базы данных.
     */
    @Override
    public void deleteAll() {
        String sql = "DELETE FROM coworking_service.conference_room";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting all conference rooms", e);
        }
    }

}

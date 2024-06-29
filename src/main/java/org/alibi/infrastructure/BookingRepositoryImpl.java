package org.alibi.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.alibi.domain.model.Booking;
import org.alibi.domain.repository.BookingRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для управления бронированиями.
 */
@RequiredArgsConstructor
@Getter
@Setter
public class BookingRepositoryImpl implements BookingRepository {

    private final Connection connection;

    /**
     * Сохраняет бронирование в базе данных.
     *
     * @param booking бронирование для сохранения.
     */
    @Override
    public void save(Booking booking) {
        String sql = "INSERT INTO coworking_service.booking (user_id, resource_id, start_time, end_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, booking.getUserId());
            preparedStatement.setLong(2, booking.getResourceId());
            preparedStatement.setObject(3, booking.getStartTime());
            preparedStatement.setObject(4, booking.getEndTime());
            preparedStatement.executeUpdate();

            try (ResultSet generateKeys = preparedStatement.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    booking.setId(generateKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving booking", e);
        }
    }

    /**
     * Находит бронирование по идентификатору.
     *
     * @param id идентификатор бронирования.
     * @return найденное бронирование или пустое значение, если бронирование не найдено.
     */
    @Override
    public Optional<Booking> findById(Long id) {
        String sql = "SELECT * FROM coworking_service.booking WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Booking booking = new Booking();
                    booking.setId(resultSet.getLong("id"));
                    booking.setUserId(resultSet.getLong("user_id"));
                    booking.setResourceId(resultSet.getLong("resource_id"));
                    booking.setStartTime(resultSet.getObject("start_time", LocalDateTime.class));
                    booking.setEndTime(resultSet.getObject("end_time", LocalDateTime.class));
                    return Optional.of(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding booking by ID", e);
        }
        return Optional.empty();
    }

    /**
     * Возвращает список всех бронирований.
     *
     * @return список бронирований.
     */
    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM coworking_service.booking";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Booking booking = new Booking();
                booking.setId(resultSet.getLong("id"));
                booking.setUserId(resultSet.getLong("user_id"));
                booking.setResourceId(resultSet.getLong("resource_id"));
                booking.setStartTime(resultSet.getObject("start_time", LocalDateTime.class));
                booking.setEndTime(resultSet.getObject("end_time", LocalDateTime.class));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error finding all bookings", e);
        }
        return bookings;
    }

    /**
     * Обновляет бронирование в базе данных.
     *
     * @param booking обновленное бронирование.
     */
    @Override
    public void update(Booking booking) {
        String sql = "UPDATE coworking_service.booking SET user_id = ?, resource_id = ?, start_time = ?, end_time = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, booking.getUserId());
            preparedStatement.setLong(2, booking.getResourceId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            preparedStatement.setLong(5, booking.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating booking", e);
        }
    }

    /**
     * Удаляет бронирование из базы данных.
     *
     * @param id идентификатор бронирования для удаления.
     */
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM coworking_service.booking WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting booking", e);
        }
    }

    /**
     * Проверяет, существует ли конфликт бронирования.
     *
     * @param booking бронирование для проверки на конфликт.
     * @return true, если существует конфликт, иначе false.
     */
    @Override
    public boolean isConflict(Booking booking) {
        String sql = "SELECT COUNT(*) FROM coworking_service.booking WHERE resource_id = ? AND " +
                "((start_time < ? AND end_time > ?) OR (start_time < ? AND end_time > ?))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, booking.getResourceId());
            preparedStatement.setObject(2, booking.getEndTime());
            preparedStatement.setObject(3, booking.getStartTime());
            preparedStatement.setObject(4, booking.getEndTime());
            preparedStatement.setObject(5, booking.getStartTime());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking booking conflict", e);
        }
        return false;
    }

    /**
     * Удаляет все бронирования из базы данных.
     */
    @Override
    public void deleteAll() {
        String sql = "DELETE FROM coworking_service.booking";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting all bookings", e);
        }
    }

}

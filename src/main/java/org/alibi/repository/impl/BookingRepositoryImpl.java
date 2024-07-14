package org.alibi.repository.impl;

import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.Booking;
import org.alibi.repository.BookingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для управления бронированиями.
 */
@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Booking> bookingRowMapper = new RowMapper<Booking>() {
        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Booking(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getLong("resource_id"),
                    rs.getObject("start_time", LocalDateTime.class),
                    rs.getObject("end_time", LocalDateTime.class)
            );
        }
    };

    @Override
    public void save(Booking booking) {
        String sql = "INSERT INTO booking (user_id, resource_id, start_time, end_time) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, booking.getUserId(), booking.getResourceId(), booking.getStartTime(), booking.getEndTime());
    }

    @Override
    public Optional<Booking> findById(Long id) {
        String sql = "SELECT * FROM booking WHERE id = ?";
        try {
            Booking booking = jdbcTemplate.queryForObject(sql, bookingRowMapper, id);
            return Optional.ofNullable(booking);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Booking> findAll() {
        String sql = "SELECT * FROM booking";
        return jdbcTemplate.query(sql, bookingRowMapper);
    }

    @Override
    public void update(Booking booking) {
        String sql = "UPDATE booking SET user_id = ?, resource_id = ?, start_time = ?, end_time = ? WHERE id = ?";
        jdbcTemplate.update(sql, booking.getUserId(), booking.getResourceId(), booking.getStartTime(), booking.getEndTime(), booking.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM booking WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean isConflict(Booking booking) {
        String sql = "SELECT COUNT(*) FROM booking WHERE resource_id = ? AND " +
                "((start_time < ? AND end_time > ?) OR (start_time < ? AND end_time > ?))";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{
                booking.getResourceId(), booking.getEndTime(), booking.getStartTime(),
                booking.getEndTime(), booking.getStartTime()
        }, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM booking";
        jdbcTemplate.update(sql);
    }
}

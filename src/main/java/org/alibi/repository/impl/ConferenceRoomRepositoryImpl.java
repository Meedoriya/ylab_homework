package org.alibi.repository.impl;

import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.repository.ConferenceRoomRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для управления конференц-залами.
 */
@Repository
@RequiredArgsConstructor
public class ConferenceRoomRepositoryImpl implements ConferenceRoomRepository {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<ConferenceRoom> conferenceRoomRowMapper = new RowMapper<ConferenceRoom>() {
        @Override
        public ConferenceRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ConferenceRoom(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getBoolean("available")
            );
        }
    };

    @Override
    public void save(ConferenceRoom conferenceRoom) {
        String sql = "INSERT INTO conference_room (name, available) VALUES (?, ?)";
        jdbcTemplate.update(sql, conferenceRoom.getName(), conferenceRoom.isAvailable());
    }

    @Override
    public Optional<ConferenceRoom> findById(Long id) {
        String sql = "SELECT * FROM conference_room WHERE id = ?";
        try {
            ConferenceRoom conferenceRoom = jdbcTemplate.queryForObject(sql, conferenceRoomRowMapper, id);
            return Optional.ofNullable(conferenceRoom);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ConferenceRoom> findAll() {
        String sql = "SELECT * FROM conference_room";
        return jdbcTemplate.query(sql, conferenceRoomRowMapper);
    }

    @Override
    public void update(ConferenceRoom conferenceRoom) {
        String sql = "UPDATE conference_room SET name = ?, available = ? WHERE id = ?";
        jdbcTemplate.update(sql, conferenceRoom.getName(), conferenceRoom.isAvailable(), conferenceRoom.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM conference_room WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM conference_room";
        jdbcTemplate.update(sql);
    }
}

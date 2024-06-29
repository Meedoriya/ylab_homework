package org.alibi.infrastructure;

import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.repository.ConferenceRoomRepository;
import org.alibi.in.DatabaseInitializer;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class ConferenceRoomRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("postgres")
            .withUsername("alibi")
            .withPassword("alibi");

    private Connection connection;
    private ConferenceRoomRepository conferenceRoomRepository;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS coworking_service");
            statement.execute("SET search_path TO coworking_service");
        }
        DatabaseInitializer.initialize(connection);
        conferenceRoomRepository = new ConferenceRoomRepositoryImpl(connection);
        conferenceRoomRepository.deleteAll();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    @DisplayName("Should save and find conference room by ID")
    void testSaveAndFindById() {
        ConferenceRoom conferenceRoom = new ConferenceRoom(null, "Conference Room 1", true);
        conferenceRoomRepository.save(conferenceRoom);

        Optional<ConferenceRoom> retrievedConferenceRoom = conferenceRoomRepository.findById(conferenceRoom.getId());
        assertThat(retrievedConferenceRoom).isPresent();
        assertThat(retrievedConferenceRoom.get().getName()).isEqualTo("Conference Room 1");
    }

    @Test
    @DisplayName("Should update conference room")
    void testUpdate() {
        ConferenceRoom conferenceRoom = new ConferenceRoom(null, "Conference Room 1", true);
        conferenceRoomRepository.save(conferenceRoom);

        conferenceRoom.setName("Updated Conference Room");
        conferenceRoomRepository.update(conferenceRoom);

        Optional<ConferenceRoom> retrievedConferenceRoom = conferenceRoomRepository.findById(conferenceRoom.getId());
        assertThat(retrievedConferenceRoom).isPresent();
        assertThat(retrievedConferenceRoom.get().getName()).isEqualTo("Updated Conference Room");
    }

    @Test
    @DisplayName("Should delete conference room")
    void testDelete() {
        ConferenceRoom conferenceRoom = new ConferenceRoom(null, "Conference Room 1", true);
        conferenceRoomRepository.save(conferenceRoom);

        conferenceRoomRepository.delete(conferenceRoom.getId());

        Optional<ConferenceRoom> retrievedConferenceRoom = conferenceRoomRepository.findById(conferenceRoom.getId());
        assertThat(retrievedConferenceRoom).isNotPresent();
    }

    @Test
    @DisplayName("Should find all conference rooms")
    void testFindAll() {
        ConferenceRoom conferenceRoom1 = new ConferenceRoom(null, "Conference Room 1", true);
        ConferenceRoom conferenceRoom2 = new ConferenceRoom(null, "Conference Room 2", true);

        conferenceRoomRepository.save(conferenceRoom1);
        conferenceRoomRepository.save(conferenceRoom2);

        List<ConferenceRoom> conferenceRooms = conferenceRoomRepository.findAll();
        assertThat(conferenceRooms).hasSize(2);
        assertThat(conferenceRooms).extracting("name").contains("Conference Room 1", "Conference Room 2");
    }
}

package org.alibi.infrastructure;

import org.alibi.domain.model.Booking;
import org.alibi.domain.repository.BookingRepository;
import org.alibi.in.DatabaseInitializer;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class BookingRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("postgres")
            .withUsername("alibi")
            .withPassword("alibi");

    private Connection connection;
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        DatabaseInitializer.initialize(connection);
        bookingRepository = new BookingRepositoryImpl(connection);
        bookingRepository.deleteAll();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    @DisplayName("Should save and find booking by ID")
    void testSaveAndFindById() {
        Booking booking = new Booking(null, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking);

        Optional<Booking> retrievedBooking = bookingRepository.findById(booking.getId());
        assertThat(retrievedBooking).isPresent();
        assertThat(retrievedBooking.get().getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should update booking")
    void testUpdate() {
        Booking booking = new Booking(null, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking);

        booking.setEndTime(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MICROS));
        bookingRepository.update(booking);

        Optional<Booking> retrievedBooking = bookingRepository.findById(booking.getId());
        assertThat(retrievedBooking).isPresent();
        assertThat(retrievedBooking.get().getEndTime().truncatedTo(ChronoUnit.MICROS)).isEqualTo(booking.getEndTime());
    }



    @Test
    @DisplayName("Should delete booking")
    void testDelete() {
        Booking booking = new Booking(null, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking);

        bookingRepository.delete(booking.getId());

        Optional<Booking> retrievedBooking = bookingRepository.findById(booking.getId());
        assertThat(retrievedBooking).isNotPresent();
    }

    @Test
    @DisplayName("Should find all bookings")
    void testFindAll() {
        Booking booking1 = new Booking(null, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Booking booking2 = new Booking(null, 2L, 2L, LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(2);
        assertThat(bookings).extracting("userId").contains(1L, 2L);
    }

    @Test
    @DisplayName("Should return true if conflict exists")
    void testIsConflict() {
        Booking booking1 = new Booking(null, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking1);

        Booking booking2 = new Booking(null, 2L, 1L, LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusHours(1).plusMinutes(30));

        boolean conflict = bookingRepository.isConflict(booking2);
        assertThat(conflict).isTrue();
    }

    @Test
    @DisplayName("Should return false if no conflict exists")
    void testNoConflict() {
        Booking booking1 = new Booking(null, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking1);

        Booking booking2 = new Booking(null, 2L, 1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        boolean conflict = bookingRepository.isConflict(booking2);
        assertThat(conflict).isFalse();
    }
}

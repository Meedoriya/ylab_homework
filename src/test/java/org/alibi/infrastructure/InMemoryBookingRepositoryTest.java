package org.alibi.infrastructure;

import org.alibi.domain.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryBookingRepositoryTest {

    private InMemoryBookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository = new InMemoryBookingRepository();
    }

    @Test
    void saveShouldAddBooking() {
        Booking booking = new Booking();
        booking.setResourceId(1L);
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(1));

        bookingRepository.save(booking);

        assertThat(bookingRepository.getBookings()).hasSize(1);
        assertThat(booking.getId()).isNotNull();
    }

    @Test
    void findByIdShouldReturnBookingIfExists() {
        Booking booking = new Booking();
        booking.setResourceId(1L);
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking);

        Optional<Booking> foundBooking = bookingRepository.findById(booking.getId());

        assertThat(foundBooking).isPresent();
        assertThat(foundBooking.get().getResourceId()).isEqualTo(1L);
    }

    @Test
    void findByIdShouldReturnEmptyIfNotExists() {
        Optional<Booking> foundBooking = bookingRepository.findById(1L);

        assertThat(foundBooking).isNotPresent();
    }

    @Test
    void findAllShouldReturnAllBookings() {
        Booking booking1 = new Booking();
        booking1.setResourceId(1L);
        booking1.setStartTime(LocalDateTime.now());
        booking1.setEndTime(LocalDateTime.now().plusHours(1));

        Booking booking2 = new Booking();
        booking2.setResourceId(2L);
        booking2.setStartTime(LocalDateTime.now());
        booking2.setEndTime(LocalDateTime.now().plusHours(1));

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAll();

        assertThat(bookings).hasSize(2);
    }

    @Test
    void updateShouldModifyExistingBooking() {
        Booking booking = new Booking();
        booking.setResourceId(1L);
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking);

        booking.setEndTime(LocalDateTime.now().plusHours(2));
        bookingRepository.update(booking);

        Optional<Booking> updatedBooking = bookingRepository.findById(booking.getId());

        assertThat(updatedBooking).isPresent();
        assertThat(updatedBooking.get().getEndTime()).isEqualTo(booking.getEndTime());
    }

    @Test
    void deleteShouldRemoveBooking() {
        Booking booking = new Booking();
        booking.setResourceId(1L);
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking);

        bookingRepository.delete(booking.getId());

        assertThat(bookingRepository.getBookings()).isEmpty();
    }

    @Test
    void isConflictShouldReturnTrueIfConflictExists() {
        Booking booking1 = new Booking();
        booking1.setResourceId(1L);
        booking1.setStartTime(LocalDateTime.now());
        booking1.setEndTime(LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setResourceId(1L);
        booking2.setStartTime(LocalDateTime.now().plusMinutes(30));
        booking2.setEndTime(LocalDateTime.now().plusHours(1).plusMinutes(30));

        boolean conflict = bookingRepository.isConflict(booking2);

        assertThat(conflict).isTrue();
    }

    @Test
    void isConflictShouldReturnFalseIfNoConflictExists() {
        Booking booking1 = new Booking();
        booking1.setResourceId(1L);
        booking1.setStartTime(LocalDateTime.now());
        booking1.setEndTime(LocalDateTime.now().plusHours(1));
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setResourceId(1L);
        booking2.setStartTime(LocalDateTime.now().plusHours(1));
        booking2.setEndTime(LocalDateTime.now().plusHours(2));

        boolean conflict = bookingRepository.isConflict(booking2);

        assertThat(conflict).isFalse();
    }
}

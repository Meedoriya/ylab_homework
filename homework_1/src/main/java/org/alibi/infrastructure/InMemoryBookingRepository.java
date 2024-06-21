package org.alibi.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.alibi.domain.model.Booking;
import org.alibi.domain.repository.BookingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для хранения бронирований в памяти.
 */
@Getter
@Setter
public class InMemoryBookingRepository implements BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();
    private Long counter = 1L;

    /**
     * Сохраняет новое бронирование.
     *
     * @param booking Бронирование для сохранения.
     */
    @Override
    public void save(Booking booking) {
        booking.setId(counter++);
        bookings.add(booking);
    }

    /**
     * Находит бронирование по его ID.
     *
     * @param id ID бронирования.
     * @return Опциональное бронирование.
     */
    @Override
    public Optional<Booking> findById(Long id) {
        return bookings.stream()
                .filter(booking -> booking.getId().equals(id)).findFirst();
    }

    /**
     * Возвращает все бронирования.
     *
     * @return Список всех бронирований.
     */
    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings);
    }

    /**
     * Обновляет существующее бронирование.
     *
     * @param booking Бронирование для обновления.
     */
    @Override
    public void update(Booking booking) {
        delete(booking.getId());
        bookings.add(booking);
    }

    /**
     * Удаляет бронирование по его ID.
     *
     * @param id ID бронирования для удаления.
     */
    @Override
    public void delete(Long id) {
        bookings.removeIf(booking -> booking.getId().equals(id));
    }

    /**
     * Проверяет наличие конфликта бронирования.
     *
     * @param booking Бронирование для проверки.
     * @return true, если обнаружен конфликт, иначе false.
     */
    @Override
    public boolean isConflict(Booking booking) {
        return bookings.stream().anyMatch(existingBooking ->
                existingBooking.getResourceId().equals(booking.getResourceId()) &&
                        existingBooking.getEndTime().isAfter(booking.getStartTime()) &&
                        existingBooking.getStartTime().isBefore(booking.getEndTime())
        );
    }
}

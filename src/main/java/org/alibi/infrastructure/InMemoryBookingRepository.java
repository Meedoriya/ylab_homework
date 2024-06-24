package org.alibi.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.alibi.domain.model.Booking;
import org.alibi.domain.repository.BookingRepository;

import java.util.*;

/**
 * Репозиторий для хранения бронирований в памяти.
 */
@Getter
@Setter
public class InMemoryBookingRepository implements BookingRepository {
    private final Map<Long, Booking> bookings = new HashMap<>();
    private Long counter = 0L;

    /**
     * Сохраняет новое бронирование.
     *
     * @param booking Бронирование для сохранения.
     */
    @Override
    public void save(Booking booking) {
        if (booking.getId() == null) {
            booking.setId(++counter);
        }
        bookings.put(booking.getId(), booking);
    }

    /**
     * Находит бронирование по его ID.
     *
     * @param id ID бронирования.
     * @return Опциональное бронирование.
     */
    @Override
    public Optional<Booking> findById(Long id) {
        return Optional.ofNullable(bookings.get(id));
    }

    /**
     * Возвращает все бронирования.
     *
     * @return Список всех бронирований.
     */
    @Override
    public List<Booking> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(bookings.values()));
    }

    /**
     * Обновляет существующее бронирование.
     *
     * @param booking Бронирование для обновления.
     */
    @Override
    public void update(Booking booking) {
        bookings.put(booking.getId(), booking);
    }

    /**
     * Удаляет бронирование по его ID.
     *
     * @param id ID бронирования для удаления.
     */
    @Override
    public void delete(Long id) {
        bookings.remove(id);
    }

    /**
     * Проверяет наличие конфликта бронирования.
     *
     * @param booking Бронирование для проверки.
     * @return true, если обнаружен конфликт, иначе false.
     */
    @Override
    public boolean isConflict(Booking booking) {
        return bookings.values().stream().anyMatch(existingBooking ->
                existingBooking.getResourceId().equals(booking.getResourceId()) &&
                        existingBooking.getEndTime().isAfter(booking.getStartTime()) &&
                        existingBooking.getStartTime().isBefore(booking.getEndTime())
                );
    }
}

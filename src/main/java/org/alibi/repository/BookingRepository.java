package org.alibi.repository;

import org.alibi.domain.model.Booking;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для управления бронированиями.
 */
public interface BookingRepository {
    /**
     * Сохраняет новое бронирование.
     * @param booking бронирование для сохранения
     */
    void save(Booking booking);

    /**
     * Ищет бронирование по его ID.
     * @param id ID бронирования
     * @return Optional с найденным бронированием, если оно существует
     */
    Optional<Booking> findById(Long id);

    /**
     * Возвращает все бронирования.
     * @return список всех бронирований
     */
    List<Booking> findAll();

    /**
     * Обновляет существующее бронирование.
     * @param booking бронирование для обновления
     */
    void update(Booking booking);

    /**
     * Удаляет бронирование по его ID.
     * @param id ID бронирования
     */
    void delete(Long id);

    /**
     * Проверяет наличие конфликтующего бронирования.
     * @param booking бронирование для проверки конфликта
     * @return true, если существует конфликтующее бронирование
     */
    boolean isConflict(Booking booking);

    /**
     * Удаляет все бронирования.
     */
    void deleteAll();
}

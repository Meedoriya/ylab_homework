package com.alibi.coworking_service.service;


import com.alibi.coworking_service.domain.dto.BookingDto;
import com.alibi.coworking_service.domain.dto.ConferenceRoomDto;
import com.alibi.coworking_service.domain.dto.WorkspaceDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс сервиса для управления бронированиями.
 */
public interface BookingService {
    /**
     * Бронирует ресурс.
     * @param userId ID пользователя
     * @param resourceId ID ресурса
     * @param startTime дата начала бронирования
     * @param endTime дата окончания бронирования
     */
    void bookResource(Long userId, Long resourceId, LocalDate startTime, LocalDate endTime);

    /**
     * Возвращает все бронирования.
     * @return список всех бронирований
     */
    List<BookingDto> getAllBookings();

    /**
     * Возвращает отфильтрованные бронирования.
     * @param date дата бронирования
     * @param userId ID пользователя
     * @param resourceId ID ресурса
     * @return список отфильтрованных бронирований
     */
    List<BookingDto> getFilteredBookings(Optional<LocalDate> date, Optional<Long> userId, Optional<Long> resourceId);

    /**
     * Возвращает бронирования пользователя.
     * @param userId ID пользователя
     * @return список бронирований пользователя
     */
    List<BookingDto> getUserBookings(Long userId);

    /**
     * Отменяет бронирование.
     * @param bookingId ID бронирования
     */
    void cancelBooking(Long bookingId);

    /**
     * Возвращает доступные рабочие места.
     * @param date дата для проверки доступности
     * @return список доступных рабочих мест
     */
    List<WorkspaceDto> getAvailableWorkspaces(LocalDate date);

    /**
     * Возвращает доступные конференц-залы.
     * @param date дата для проверки доступности
     * @return список доступных конференц-залов
     */
    List<ConferenceRoomDto> getAvailableConferenceRooms(LocalDate date);
}

package org.alibi.application;

import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.Booking;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.BookingRepository;
import org.alibi.domain.repository.ConferenceRoomRepository;
import org.alibi.domain.repository.WorkspaceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для управления бронированиями.
 */
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AuthorizationService authorizationService;
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final WorkspaceRepository workspaceRepository;

    /**
     * Возвращает список доступных рабочих мест на указанную дату.
     *
     * @param date Дата для проверки доступности.
     * @return Список доступных рабочих мест.
     */
    public List<Workspace> getAvailableWorkspaces(LocalDate date) {
        List<Long> bookedWorkspaceIds = bookingRepository.findAll().stream()
                .filter(booking -> booking.getStartTime().toLocalDate().equals(date))
                .map(Booking::getResourceId)
                .toList();

        return workspaceRepository.findAll().stream()
                .filter(workspace -> !bookedWorkspaceIds.contains(workspace.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список доступных конференц-залов на указанную дату.
     *
     * @param date Дата для проверки доступности.
     * @return Список доступных конференц-залов.
     */
    public List<ConferenceRoom> getAvailableConferenceRooms(LocalDate date) {
        List<Long> bookedConferenceRooms = bookingRepository.findAll().stream()
                .filter(booking -> booking.getStartTime().toLocalDate().equals(date))
                .map(Booking::getResourceId)
                .collect(Collectors.toList());

        return conferenceRoomRepository.findAll().stream()
                .filter(conferenceRoom -> !bookedConferenceRooms.contains(conferenceRoom.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Бронирует ресурс (рабочее место или конференц-зал) для пользователя.
     *
     * @param userId     ID пользователя.
     * @param resourceId ID ресурса.
     * @param startTime  Время начала бронирования.
     * @param endTime    Время окончания бронирования.
     * @throws IllegalArgumentException если время начала после или равно времени окончания, ресурс не найден или обнаружен конфликт бронирования.
     */
    public void bookResource(Long userId, Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        boolean isWorkspace = workspaceRepository.findById(resourceId).isPresent();
        boolean isConferenceRoom = conferenceRoomRepository.findById(resourceId).isPresent();

        if (!isWorkspace && !isConferenceRoom) {
            throw new IllegalArgumentException("Resource not found");
        }

        List<Booking> existingBookings = bookingRepository.findAll().stream()
                .filter(booking -> booking.getResourceId().equals(resourceId))
                .filter(booking -> booking.getEndTime().isAfter(startTime) && booking.getStartTime().isBefore(endTime))
                .toList();

        if (!existingBookings.isEmpty()) {
            throw new IllegalArgumentException("Booking conflict detected");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setResourceId(resourceId);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);

        bookingRepository.save(booking);
    }


    /**
     * Возвращает список всех бронирований, доступных пользователю.
     *
     * @param user Пользователь, запрашивающий список бронирований.
     * @return Список всех бронирований.
     * @throws SecurityException если пользователь не является администратором.
     */
    public List<Booking> getAllBookings(User user) {
        if (authorizationService.isAdmin(user)) {
            return bookingRepository.findAll();
        } else {
            throw new SecurityException("Only admin can view all bookings");
        }
    }

    /**
     * Возвращает отфильтрованный список бронирований по дате, ID пользователя или ID ресурса.
     *
     * @param date      Опциональная дата для фильтрации.
     * @param userId    Опциональный ID пользователя для фильтрации.
     * @param resourceId Опциональный ID ресурса для фильтрации.
     * @return Отфильтрованный список бронирований.
     */
    public List<Booking> getFilteredBookings(Optional<LocalDate> date, Optional<Long> userId, Optional<Long> resourceId) {
        return bookingRepository.findAll().stream()
                .filter(booking -> date.map(d -> booking.getStartTime().toLocalDate().equals(d)).orElse(true))
                .filter(booking -> userId.map(id -> booking.getUserId().equals(id)).orElse(true))
                .filter(booking -> resourceId.map(id -> booking.getResourceId().equals(id)).orElse(true))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список бронирований, сделанных конкретным пользователем.
     *
     * @param user Пользователь, чьи бронирования нужно получить.
     * @return Список бронирований пользователя.
     */
    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getUserId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Отменяет бронирование по его ID.
     *
     * @param user Пользователь, запрашивающий отмену бронирования.
     * @param id   ID бронирования.
     * @throws SecurityException если пользователь не является администратором или не является владельцем бронирования.
     * @throws IllegalArgumentException если бронирование не найдено.
     */
    public void cancelBooking(User user, Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            if (authorizationService.isAdmin(user) || booking.get().getUserId().equals(user.getId())) {
                bookingRepository.delete(id);
            } else {
                throw new SecurityException("You can cancel only your own bookings.");
            }
        } else {
            throw new IllegalArgumentException("Booking not found.");
        }
    }
}

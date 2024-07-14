package org.alibi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.alibi.domain.dto.BookingDto;
import org.alibi.domain.dto.ConferenceRoomDto;
import org.alibi.domain.dto.WorkspaceDto;
import org.alibi.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для управления бронированиями.
 */
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Api(tags = "Booking Controller", description = "API for managing bookings")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Создает новое бронирование.
     *
     * @param bookingDto DTO бронирования
     * @return ResponseEntity с статусом 201
     */
    @PostMapping
    @ApiOperation(value = "Create a new booking", notes = "Creates a new booking for a resource")
    public ResponseEntity<Void> bookResource(@RequestBody BookingDto bookingDto) {
        bookingService.bookResource(bookingDto.getUserId(), bookingDto.getResourceId(),
                bookingDto.getStartTime().toLocalDate(), bookingDto.getEndTime().toLocalDate());
        return ResponseEntity.status(201).build();
    }

    /**
     * Возвращает список всех бронирований.
     *
     * @return ResponseEntity со списком всех бронирований
     */
    @GetMapping
    @ApiOperation(value = "Get all bookings", notes = "Returns a list of all bookings")
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        List<BookingDto> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    /**
     * Возвращает отфильтрованный список бронирований.
     *
     * @param date дата бронирования
     * @param userId ID пользователя
     * @param resourceId ID ресурса
     * @return ResponseEntity с отфильтрованным списком бронирований
     */
    @GetMapping("/filter")
    @ApiOperation(value = "Get filtered bookings", notes = "Returns a list of bookings filtered by date, user ID, and/or resource ID")
    public ResponseEntity<List<BookingDto>> getFilteredBookings(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long resourceId) {
        List<BookingDto> bookings = bookingService.getFilteredBookings(Optional.ofNullable(date), Optional.ofNullable(userId), Optional.ofNullable(resourceId));
        return ResponseEntity.ok(bookings);
    }

    /**
     * Возвращает список бронирований для конкретного пользователя.
     *
     * @param userId ID пользователя
     * @return ResponseEntity с списком бронирований пользователя
     */
    @GetMapping("/user/{userId}")
    @ApiOperation(value = "Get user bookings", notes = "Returns a list of bookings for a specific user ID")
    public ResponseEntity<List<BookingDto>> getUserBookings(@PathVariable Long userId) {
        List<BookingDto> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    /**
     * Отменяет бронирование по его ID.
     *
     * @param bookingId ID бронирования
     * @return ResponseEntity с статусом 204
     */
    @DeleteMapping("/{bookingId}")
    @ApiOperation(value = "Cancel a booking", notes = "Cancels a booking by its ID")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Возвращает список доступных рабочих мест на конкретную дату.
     *
     * @param date дата для проверки доступности
     * @return ResponseEntity со списком доступных рабочих мест
     */
    @GetMapping("/available-workspaces")
    @ApiOperation(value = "Get available workspaces", notes = "Returns a list of available workspaces for a specific date")
    public ResponseEntity<List<WorkspaceDto>> getAvailableWorkspaces(@RequestParam LocalDate date) {
        List<WorkspaceDto> workspaces = bookingService.getAvailableWorkspaces(date);
        return ResponseEntity.ok(workspaces);
    }

    /**
     * Возвращает список доступных конференц-залов на конкретную дату.
     *
     * @param date дата для проверки доступности
     * @return ResponseEntity со списком доступных конференц-залов
     */
    @GetMapping("/available-conference-rooms")
    @ApiOperation(value = "Get available conference rooms", notes = "Returns a list of available conference rooms for a specific date")
    public ResponseEntity<List<ConferenceRoomDto>> getAvailableConferenceRooms(@RequestParam LocalDate date) {
        List<ConferenceRoomDto> conferenceRooms = bookingService.getAvailableConferenceRooms(date);
        return ResponseEntity.ok(conferenceRooms);
    }
}

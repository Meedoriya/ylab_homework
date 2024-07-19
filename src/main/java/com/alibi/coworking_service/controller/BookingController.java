package com.alibi.coworking_service.controller;


import com.alibi.coworking_service.domain.dto.BookingDto;
import com.alibi.coworking_service.domain.dto.ConferenceRoomDto;
import com.alibi.coworking_service.domain.dto.WorkspaceDto;
import com.alibi.coworking_service.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
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
public class BookingController {

    private final BookingService bookingService;

    /**
     * Создает новое бронирование.
     *
     * @param bookingDto DTO бронирования
     * @return ResponseEntity с статусом 201
     */
    @PostMapping
    @Operation(summary = "Create a new booking", description = "Creates a new booking for a resource")
    public ResponseEntity<Void> bookResource(@RequestBody BookingDto bookingDto) {
        bookingService.bookResource(
                bookingDto.getUserId(),
                bookingDto.getResourceId(),
                bookingDto.getStartTime().toLocalDate(),
                bookingDto.getEndTime().toLocalDate()
        );
        return ResponseEntity.status(201).build();
    }


    /**
     * Возвращает список всех бронирований.
     *
     * @return ResponseEntity со списком всех бронирований
     */
    @GetMapping
    @Operation(summary = "Get all bookings", description = "Returns a list of all bookings")
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        List<BookingDto> allBookings = bookingService.getAllBookings();
        return ResponseEntity.ok(allBookings);
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
    @Operation(summary = "Get filtered bookings", description = "Returns a list of bookings filtered by date, user ID, and/or resource ID")
    public ResponseEntity<List<BookingDto>> getFilteredBookings(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long resourceId) {

        List<BookingDto> bookings = bookingService.getFilteredBookings(
                Optional.ofNullable(date),
                userId.describeConstable(),
                resourceId.describeConstable());

        return ResponseEntity.ok(bookings);
    }

    /**
     * Возвращает список бронирований для конкретного пользователя.
     *
     * @param userId ID пользователя
     * @return ResponseEntity с списком бронирований пользователя
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user bookings", description = "Returns a list of bookings for a specific user ID")
    public ResponseEntity<List<BookingDto>> getUserBookings(@PathVariable Long userId) {
        List<BookingDto> userBookings = bookingService.getUserBookings(userId);

        return ResponseEntity.ok(userBookings);
    }

    /**
     * Отменяет бронирование по его ID.
     *
     * @param bookingId ID бронирования
     * @return ResponseEntity с статусом 204
     */
    @DeleteMapping("/{bookingId}")
    @Operation(summary = "Cancel a booking", description = "Cancels a booking by its ID")
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
    @Operation(summary = "Get available workspaces", description = "Returns a list of available workspaces for a specific date")
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
    @Operation(summary = "Get available conference rooms", description = "Returns a list of available conference rooms for a specific date")
    public ResponseEntity<List<ConferenceRoomDto>> getAvailableConferenceRooms(@RequestParam LocalDate date) {
        List<ConferenceRoomDto> conferenceRooms = bookingService.getAvailableConferenceRooms(date);

        return ResponseEntity.ok(conferenceRooms);
    }

}

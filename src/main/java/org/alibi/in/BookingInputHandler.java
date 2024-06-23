package org.alibi.in;

import org.alibi.application.BookingService;
import org.alibi.domain.model.User;
import org.alibi.out.ConsoleOutput;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;

/**
 * Обработчик ввода для операций с бронированиями.
 */
public class BookingInputHandler {
    private final BookingService bookingService;
    private final Scanner scanner;
    private final ConsoleOutput consoleOutput;

    /**
     * Конструктор.
     *
     * @param bookingService Сервис для работы с бронированиями.
     * @param consoleOutput  Объект для вывода на консоль.
     */
    public BookingInputHandler(BookingService bookingService, ConsoleOutput consoleOutput) {
        this.bookingService = bookingService;
        this.scanner = new Scanner(System.in);
        this.consoleOutput = consoleOutput;
    }

    /**
     * Метод для бронирования ресурса пользователем.
     *
     * @param user Пользователь, который хочет забронировать ресурс.
     */
    public void bookResource(User user) {
        System.out.print("Enter resource ID: ");
        Long resourceId = Long.parseLong(scanner.nextLine());
        System.out.print("Enter start time (yyyy-MM-ddTHH:mm): ");
        LocalDateTime startTime = LocalDateTime.parse(scanner.nextLine());
        System.out.print("Enter end time (yyyy-MM-ddTHH:mm): ");
        LocalDateTime endTime = LocalDateTime.parse(scanner.nextLine());

        try {
            bookingService.bookResource(user.getId(), resourceId, startTime, endTime);
            System.out.println("Booking successful.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для отмены бронирования пользователем.
     *
     * @param user Пользователь, который хочет отменить бронирование.
     */
    public void cancelBooking(User user) {
        System.out.print("Enter booking ID to cancel: ");
        Long bookingId = Long.parseLong(scanner.nextLine());

        try {
            bookingService.cancelBooking(user, bookingId);
            System.out.println("Booking cancelled successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Метод для фильтрации бронирований по дате, ID пользователя или ID ресурса.
     */
    public void filterBookings() {
        System.out.print("Enter date (yyyy-MM-dd) or leave blank: ");
        String dateInput = scanner.nextLine();
        Optional<LocalDate> date = dateInput.isEmpty() ? Optional.empty() : Optional.of(LocalDate.parse(dateInput));

        System.out.print("Enter user ID or leave blank: ");
        String userIdInput = scanner.nextLine();
        Optional<Long> userId = userIdInput.isEmpty() ? Optional.empty() : Optional.of(Long.parseLong(userIdInput));

        System.out.print("Enter resource ID or leave blank: ");
        String resourceIdInput = scanner.nextLine();
        Optional<Long> resourceId = resourceIdInput.isEmpty() ? Optional.empty() : Optional.of(Long.parseLong(resourceIdInput));

        consoleOutput.printBookings(bookingService.getFilteredBookings(date, userId, resourceId));
    }
}


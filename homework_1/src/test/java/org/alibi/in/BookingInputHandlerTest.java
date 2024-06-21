package org.alibi.in;

import org.alibi.application.BookingService;
import org.alibi.domain.model.User;
import org.alibi.out.ConsoleOutput;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingInputHandlerTest {

    private BookingService bookingService;
    private ConsoleOutput consoleOutput;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        bookingService = Mockito.mock(BookingService.class);
        consoleOutput = Mockito.mock(ConsoleOutput.class);
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testBookResource() {
        String input = "1\n2023-06-21T10:00\n2023-06-21T12:00\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        BookingInputHandler bookingInputHandler = new BookingInputHandler(bookingService, consoleOutput);
        User user = new User();
        user.setId(1L);

        bookingInputHandler.bookResource(user);

        verify(bookingService).bookResource(eq(1L), eq(1L), eq(LocalDateTime.of(2023, 6, 21, 10, 0)), eq(LocalDateTime.of(2023, 6, 21, 12, 0)));
        assertThat(outContent.toString()).contains("Booking successful.");
    }

    @Test
    void testCancelBooking() {
        String input = "1\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        BookingInputHandler bookingInputHandler = new BookingInputHandler(bookingService, consoleOutput);
        User user = new User();
        user.setId(1L);

        bookingInputHandler.cancelBooking(user);

        verify(bookingService).cancelBooking(eq(user), eq(1L));
        assertThat(outContent.toString()).contains("Booking cancelled successfully.");
    }

    @Test
    void testFilterBookings() {
        String input = "2023-06-21\n1\n1\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        BookingInputHandler bookingInputHandler = new BookingInputHandler(bookingService, consoleOutput);

        bookingInputHandler.filterBookings();

        verify(bookingService).getFilteredBookings(
                eq(Optional.of(LocalDate.of(2023, 6, 21))),
                eq(Optional.of(1L)),
                eq(Optional.of(1L))
        );
        verify(consoleOutput).printBookings(anyList());
    }
}

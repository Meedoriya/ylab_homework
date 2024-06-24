package org.alibi.out;

import org.alibi.domain.model.Booking;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.Workspace;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleOutputTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private ConsoleOutput consoleOutput;

    @BeforeEach
    void setUp() {
        consoleOutput = new ConsoleOutput();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    @DisplayName("Should print workspace names")
    void printWorkspacesShouldPrintWorkspaceNames() {
        Workspace workspace1 = new Workspace(1L, "Workspace 1", true);
        Workspace workspace2 = new Workspace(2L, "Workspace 2", true);

        consoleOutput.printWorkspaces(List.of(workspace1, workspace2));

        String output = outContent.toString();
        assertThat(output).contains("Available Workspaces:");
        assertThat(output).contains("- Workspace 1");
        assertThat(output).contains("- Workspace 2");
    }

    @Test
    @DisplayName("Should print conference room names")
    void printConferenceRoomsShouldPrintConferenceRoomNames() {
        ConferenceRoom conferenceRoom1 = new ConferenceRoom(1L, "Conference Room 1", true);
        ConferenceRoom conferenceRoom2 = new ConferenceRoom(2L, "Conference Room 2", true);

        consoleOutput.printConferenceRooms(List.of(conferenceRoom1, conferenceRoom2));

        String output = outContent.toString();
        assertThat(output).contains("Available Conference Rooms:");
        assertThat(output).contains("- Conference Room 1");
        assertThat(output).contains("- Conference Room 2");
    }

    @Test
    @DisplayName("Should print booking details")
    void printBookingsShouldPrintBookingDetails() {
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setUserId(1L);
        booking1.setResourceId(1L);
        booking1.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));
        booking1.setEndTime(LocalDateTime.of(2023, 1, 1, 12, 0));

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setUserId(2L);
        booking2.setResourceId(2L);
        booking2.setStartTime(LocalDateTime.of(2023, 1, 2, 14, 0));
        booking2.setEndTime(LocalDateTime.of(2023, 1, 2, 16, 0));

        consoleOutput.printBookings(List.of(booking1, booking2));

        String output = outContent.toString();
        assertThat(output).contains("Bookings:");
        assertThat(output).contains("- Booking ID: 1, User ID: 1, Resource ID: 1, Start Time: 2023-01-01T10:00, End Time: 2023-01-01T12:00");
        assertThat(output).contains("- Booking ID: 2, User ID: 2, Resource ID: 2, Start Time: 2023-01-02T14:00, End Time: 2023-01-02T16:00");
    }
}

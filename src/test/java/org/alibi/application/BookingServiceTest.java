package org.alibi.application;

import org.alibi.domain.model.Booking;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.BookingRepository;
import org.alibi.domain.repository.ConferenceRoomRepository;
import org.alibi.domain.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    private BookingRepository bookingRepository;
    private ConferenceRoomRepository conferenceRoomRepository;
    private WorkspaceRepository workspaceRepository;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingRepository = Mockito.mock(BookingRepository.class);
        conferenceRoomRepository = Mockito.mock(ConferenceRoomRepository.class);
        workspaceRepository = Mockito.mock(WorkspaceRepository.class);
        bookingService = new BookingService(bookingRepository, conferenceRoomRepository, workspaceRepository);
    }

    @Test
    @DisplayName("Should get available workspaces for a date")
    void shouldGetAvailableWorkspacesForDate() {
        LocalDate date = LocalDate.now();
        Workspace workspace = new Workspace(1L, "Workspace 1", true);
        when(workspaceRepository.findAll()).thenReturn(List.of(workspace));
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        List<Workspace> availableWorkspaces = bookingService.getAvailableWorkspaces(date);

        assertThat(availableWorkspaces).contains(workspace);
    }

    @Test
    @DisplayName("Should get available conference rooms for a date")
    void shouldGetAvailableConferenceRoomsForDate() {
        LocalDate date = LocalDate.now();
        ConferenceRoom conferenceRoom = new ConferenceRoom(1L, "Conference Room 1", true);
        when(conferenceRoomRepository.findAll()).thenReturn(List.of(conferenceRoom));
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        List<ConferenceRoom> availableConferenceRooms = bookingService.getAvailableConferenceRooms(date);

        assertThat(availableConferenceRooms).contains(conferenceRoom);
    }

    @Test
    @DisplayName("Should book a resource")
    void shouldBookResource() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(1);

        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.of(new Workspace()));
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        bookingService.bookResource(userId, resourceId, startTime, endTime);

        Mockito.verify(bookingRepository).save(Mockito.any(Booking.class));
    }

    @Test
    @DisplayName("Should throw exception when booking resource with conflict")
    void shouldThrowExceptionWhenBookingResourceWithConflict() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(1);
        Booking existingBooking = new Booking(1L, userId, resourceId, startTime, endTime);

        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.of(new Workspace()));
        when(bookingRepository.findAll()).thenReturn(List.of(existingBooking));

        assertThatThrownBy(() -> bookingService.bookResource(userId, resourceId, startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Booking conflict detected");
    }

    @Test
    @DisplayName("Should throw exception when booking resource with invalid time")
    void shouldThrowExceptionWhenBookingResourceWithInvalidTime() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.minusHours(1);

        assertThatThrownBy(() -> bookingService.bookResource(userId, resourceId, startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Start time must be before end time");
    }

    @Test
    @DisplayName("Should throw exception when booking non-existent resource")
    void shouldThrowExceptionWhenBookingNonExistentResource() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(1);

        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.empty());
        when(conferenceRoomRepository.findById(resourceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.bookResource(userId, resourceId, startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Resource not found");
    }

    @Test
    @DisplayName("Should get all bookings")
    void shouldGetAllBookings() {
        Booking booking = new Booking(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.getAllBookings();

        assertThat(bookings).contains(booking);
    }

    @Test
    @DisplayName("Should get filtered bookings by date")
    void shouldGetFilteredBookingsByDate() {
        LocalDate date = LocalDate.now();
        Booking booking = new Booking(1L, 1L, 1L, date.atStartOfDay(), date.atStartOfDay().plusHours(1));
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.getFilteredBookings(Optional.of(date), Optional.empty(), Optional.empty());

        assertThat(bookings).contains(booking);
    }

    @Test
    @DisplayName("Should get filtered bookings by user ID")
    void shouldGetFilteredBookingsByUserId() {
        Long userId = 1L;
        Booking booking = new Booking(1L, userId, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.getFilteredBookings(Optional.empty(), Optional.of(userId), Optional.empty());

        assertThat(bookings).contains(booking);
    }

    @Test
    @DisplayName("Should get filtered bookings by resource ID")
    void shouldGetFilteredBookingsByResourceId() {
        Long resourceId = 1L;
        Booking booking = new Booking(1L, 1L, resourceId, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.getFilteredBookings(Optional.empty(), Optional.empty(), Optional.of(resourceId));

        assertThat(bookings).contains(booking);
    }

    @Test
    @DisplayName("Should get user bookings")
    void shouldGetUserBookings() {
        Long userId = 1L;
        User user = new User(userId, "username", "password");
        Booking booking = new Booking(1L, userId, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.getUserBookings(user);

        assertThat(bookings).contains(booking);
    }

    @Test
    @DisplayName("Should cancel booking")
    void shouldCancelBooking() {
        Long bookingId = 1L;
        Booking booking = new Booking(bookingId, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(new User(), bookingId);

        Mockito.verify(bookingRepository).delete(bookingId);
    }

    @Test
    @DisplayName("Should throw exception when canceling non-existent booking")
    void shouldThrowExceptionWhenCancelingNonExistentBooking() {
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking(new User(), bookingId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Booking not found.");
    }
}


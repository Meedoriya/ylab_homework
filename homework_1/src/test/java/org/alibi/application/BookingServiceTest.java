package org.alibi.application;

import org.alibi.domain.model.Booking;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.BookingRepository;
import org.alibi.domain.repository.ConferenceRoomRepository;
import org.alibi.domain.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;
    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAvailableWorkspacesShouldReturnAvailableWorkspaces() {
        LocalDate date = LocalDate.now();
        Workspace workspace1 = new Workspace(1L, "Workspace 1", true);
        Workspace workspace2 = new Workspace(2L, "Workspace 2", true);
        Booking booking = new Booking();
        booking.setResourceId(1L);
        booking.setStartTime(LocalDateTime.of(date, LocalDateTime.now().toLocalTime()));
        when(bookingRepository.findAll()).thenReturn(List.of(booking));
        when(workspaceRepository.findAll()).thenReturn(List.of(workspace1, workspace2));

        List<Workspace> availableWorkspaces = bookingService.getAvailableWorkspaces(date);

        assertThat(availableWorkspaces).containsExactly(workspace2);
    }

    @Test
    void getAvailableConferenceRoomsShouldReturnAvailableConferenceRooms() {
        LocalDate date = LocalDate.now();
        ConferenceRoom conferenceRoom1 = new ConferenceRoom(1L, "Conference Room 1", true);
        ConferenceRoom conferenceRoom2 = new ConferenceRoom(2L, "Conference Room 2", true);
        Booking booking = new Booking();
        booking.setResourceId(1L);
        booking.setStartTime(LocalDateTime.of(date, LocalDateTime.now().toLocalTime()));
        when(bookingRepository.findAll()).thenReturn(List.of(booking));
        when(conferenceRoomRepository.findAll()).thenReturn(List.of(conferenceRoom1, conferenceRoom2));

        List<ConferenceRoom> availableConferenceRooms = bookingService.getAvailableConferenceRooms(date);

        assertThat(availableConferenceRooms).containsExactly(conferenceRoom2);
    }

    @Test
    void bookResourceShouldThrowExceptionWhenStartTimeAfterEndTime() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        assertThatThrownBy(() -> bookingService.bookResource(userId, resourceId, startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Start time must be before end time");
    }

    @Test
    void bookResourceShouldThrowExceptionWhenResourceNotFound() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.empty());
        when(conferenceRoomRepository.findById(resourceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.bookResource(userId, resourceId, startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Resource not found");
    }

    @Test
    void bookResourceShouldThrowExceptionWhenBookingConflictDetected() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        Booking existingBooking = new Booking();
        existingBooking.setResourceId(resourceId);
        existingBooking.setStartTime(startTime.minusMinutes(30));
        existingBooking.setEndTime(endTime.plusMinutes(30));
        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.of(new Workspace()));
        when(bookingRepository.findAll()).thenReturn(List.of(existingBooking));

        assertThatThrownBy(() -> bookingService.bookResource(userId, resourceId, startTime, endTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Booking conflict detected");
    }

    @Test
    void bookResourceShouldSaveBookingWhenNoConflict() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.of(new Workspace()));
        when(bookingRepository.findAll()).thenReturn(List.of());

        bookingService.bookResource(userId, resourceId, startTime, endTime);

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void getAllBookingsShouldThrowExceptionWhenUserIsNotAdmin() {
        User user = new User();
        when(authorizationService.isAdmin(user)).thenReturn(false);

        assertThatThrownBy(() -> bookingService.getAllBookings(user))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Only admin can view all bookings");
    }

    @Test
    void getAllBookingsShouldReturnAllBookingsWhenUserIsAdmin() {
        User user = new User();
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        when(authorizationService.isAdmin(user)).thenReturn(true);
        when(bookingRepository.findAll()).thenReturn(List.of(booking1, booking2));

        List<Booking> bookings = bookingService.getAllBookings(user);

        assertThat(bookings).containsExactly(booking1, booking2);
    }

    @Test
    void getFilteredBookingsShouldReturnFilteredBookings() {
        LocalDate date = LocalDate.now();
        Long userId = 1L;
        Long resourceId = 1L;
        Booking booking = new Booking();
        booking.setStartTime(LocalDateTime.of(date, LocalDateTime.now().toLocalTime()));
        booking.setUserId(userId);
        booking.setResourceId(resourceId);
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.getFilteredBookings(Optional.of(date), Optional.of(userId), Optional.of(resourceId));

        assertThat(bookings).containsExactly(booking);
    }

    @Test
    void getUserBookingsShouldReturnUserBookings() {
        User user = new User();
        user.setId(1L);
        Booking booking = new Booking();
        booking.setUserId(1L);
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.getUserBookings(user);

        assertThat(bookings).containsExactly(booking);
    }

    @Test
    void cancelBookingShouldThrowExceptionWhenBookingNotFound() {
        User user = new User();
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking(user, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Booking not found");
    }

    @Test
    void cancelBookingShouldThrowExceptionWhenUserIsNotAdminAndNotOwner() {
        User user = new User();
        user.setId(1L);
        Booking booking = new Booking();
        booking.setUserId(2L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(authorizationService.isAdmin(user)).thenReturn(false);

        assertThatThrownBy(() -> bookingService.cancelBooking(user, 1L))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("You can cancel only your own bookings");
    }

    @Test
    void cancelBookingShouldDeleteBookingWhenUserIsAdmin() {
        User user = new User();
        Booking booking = new Booking();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(authorizationService.isAdmin(user)).thenReturn(true);

        bookingService.cancelBooking(user, 1L);

        verify(bookingRepository).delete(1L);
    }

    @Test
    void cancelBookingShouldDeleteBookingWhenUserIsOwner() {
        User user = new User();
        user.setId(1L);
        Booking booking = new Booking();
        booking.setUserId(1L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(authorizationService.isAdmin(user)).thenReturn(false);

        bookingService.cancelBooking(user, 1L);

        verify(bookingRepository).delete(1L);
    }

    @Test
    void bookResourceShouldSaveBookingWhenBookingWorkspace() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(1);
        Workspace workspace = new Workspace(resourceId, "Workspace", true);

        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.of(workspace));
        when(conferenceRoomRepository.findById(resourceId)).thenReturn(Optional.empty());
        when(bookingRepository.findAll()).thenReturn(List.of());

        bookingService.bookResource(userId, resourceId, startTime, endTime);

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void bookResourceShouldSaveBookingWhenBookingConferenceRoom() {
        Long userId = 1L;
        Long resourceId = 2L;
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(1);
        ConferenceRoom conferenceRoom = new ConferenceRoom(resourceId, "Conference Room", true);

        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.empty());
        when(conferenceRoomRepository.findById(resourceId)).thenReturn(Optional.of(conferenceRoom));
        when(bookingRepository.findAll()).thenReturn(List.of());

        bookingService.bookResource(userId, resourceId, startTime, endTime);

        verify(bookingRepository).save(any(Booking.class));
    }
}

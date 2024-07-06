package org.alibi.application;

import org.alibi.domain.model.Booking;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.BookingRepository;
import org.alibi.domain.repository.ConferenceRoomRepository;
import org.alibi.domain.repository.WorkspaceRepository;
import org.alibi.dto.BookingDto;
import org.alibi.dto.ConferenceRoomDto;
import org.alibi.dto.UserDto;
import org.alibi.dto.WorkspaceDto;
import org.alibi.mapper.BookingMapper;
import org.alibi.mapper.ConferenceRoomMapper;
import org.alibi.mapper.WorkspaceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private BookingRepository bookingRepository;
    private ConferenceRoomRepository conferenceRoomRepository;
    private WorkspaceRepository workspaceRepository;
    private BookingService bookingService;
    private BookingMapper bookingMapper = BookingMapper.INSTANCE;
    private ConferenceRoomMapper conferenceRoomMapper = ConferenceRoomMapper.INSTANCE;
    private WorkspaceMapper workspaceMapper = WorkspaceMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        conferenceRoomRepository = mock(ConferenceRoomRepository.class);
        workspaceRepository = mock(WorkspaceRepository.class);
        bookingService = new BookingService(bookingRepository, conferenceRoomRepository, workspaceRepository);
    }

    @Test
    @DisplayName("Should get available workspaces for a specific date")
    void getAvailableWorkspaces() {
        LocalDate date = LocalDate.now();
        List<Workspace> workspaces = List.of(new Workspace(1L, "Workspace 1", true));
        when(workspaceRepository.findAll()).thenReturn(workspaces);
        when(bookingRepository.findAll()).thenReturn(List.of());

        List<WorkspaceDto> availableWorkspaces = bookingService.getAvailableWorkspaces(date);

        assertThat(availableWorkspaces).hasSize(1);
        assertThat(availableWorkspaces.get(0).getName()).isEqualTo("Workspace 1");
    }

    @Test
    @DisplayName("Should get available conference rooms for a specific date")
    void getAvailableConferenceRooms() {
        LocalDate date = LocalDate.now();
        List<ConferenceRoom> conferenceRooms = List.of(new ConferenceRoom(1L, "Conference Room 1", true));
        when(conferenceRoomRepository.findAll()).thenReturn(conferenceRooms);
        when(bookingRepository.findAll()).thenReturn(List.of());

        List<ConferenceRoomDto> availableConferenceRooms = bookingService.getAvailableConferenceRooms(date);

        assertThat(availableConferenceRooms).hasSize(1);
        assertThat(availableConferenceRooms.get(0).getName()).isEqualTo("Conference Room 1");
    }

    @Test
    @DisplayName("Should book resource successfully")
    void bookResource() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(1);

        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.of(new Workspace()));
        when(bookingRepository.findAll()).thenReturn(List.of());

        bookingService.bookResource(userId, resourceId, startTime, endTime);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Should throw exception when start time is after end time")
    void bookResource_InvalidTime() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusHours(2);
        LocalDateTime endTime = startTime.minusHours(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.bookResource(userId, resourceId, startTime, endTime));
        assertThat(exception.getMessage()).isEqualTo("Start time must be before end time");
    }

    @Test
    @DisplayName("Should throw exception when resource not found")
    void bookResource_ResourceNotFound() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(1);

        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.empty());
        when(conferenceRoomRepository.findById(resourceId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.bookResource(userId, resourceId, startTime, endTime));
        assertThat(exception.getMessage()).isEqualTo("Resource not found");
    }

    @Test
    @DisplayName("Should throw exception when booking conflict detected")
    void bookResource_BookingConflict() {
        Long userId = 1L;
        Long resourceId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(1);

        when(workspaceRepository.findById(resourceId)).thenReturn(Optional.of(new Workspace()));
        when(bookingRepository.findAll()).thenReturn(List.of(new Booking(1L, userId, resourceId, startTime, endTime)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.bookResource(userId, resourceId, startTime, endTime));
        assertThat(exception.getMessage()).isEqualTo("Booking conflict detected");
    }

    @Test
    @DisplayName("Should return all bookings")
    void getAllBookings() {
        List<Booking> bookings = List.of(new Booking(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<BookingDto> allBookings = bookingService.getAllBookings();

        assertThat(allBookings).hasSize(1);
        assertThat(allBookings.get(0).getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should return filtered bookings")
    void getFilteredBookings() {
        LocalDate date = LocalDate.now();
        Long userId = 1L;
        Long resourceId = 1L;
        List<Booking> bookings = List.of(new Booking(1L, userId, resourceId, date.atStartOfDay(), date.atTime(23, 59)));
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<BookingDto> filteredBookings = bookingService.getFilteredBookings(Optional.of(date), Optional.of(userId), Optional.of(resourceId));

        assertThat(filteredBookings).hasSize(1);
        assertThat(filteredBookings.get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should return user bookings")
    void getUserBookings() {
        Long userId = 1L;
        List<Booking> bookings = List.of(new Booking(1L, userId, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        when(bookingRepository.findAll()).thenReturn(bookings);

        UserDto userDto = new UserDto(userId, "testUser");
        List<BookingDto> userBookings = bookingService.getUserBookings(userDto);

        assertThat(userBookings).hasSize(1);
        assertThat(userBookings.get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should cancel booking successfully")
    void cancelBooking() {
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(new Booking()));

        UserDto userDto = new UserDto(1L, "admin");
        bookingService.cancelBooking(userDto, bookingId);

        verify(bookingRepository, times(1)).delete(bookingId);
    }

    @Test
    @DisplayName("Should throw exception when booking not found")
    void cancelBooking_BookingNotFound() {
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        UserDto userDto = new UserDto(1L, "admin");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.cancelBooking(userDto, bookingId));
        assertThat(exception.getMessage()).isEqualTo("Booking not found.");
    }
}

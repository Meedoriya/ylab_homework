package org.alibi.service.impl;

import org.alibi.domain.dto.BookingDto;
import org.alibi.domain.dto.ConferenceRoomDto;
import org.alibi.domain.dto.WorkspaceDto;
import org.alibi.domain.model.Booking;
import org.alibi.mapper.BookingMapper;
import org.alibi.mapper.WorkspaceMapper;
import org.alibi.mapper.ConferenceRoomMapper;
import org.alibi.repository.BookingRepository;
import org.alibi.repository.ConferenceRoomRepository;
import org.alibi.repository.WorkspaceRepository;
import org.alibi.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления бронированиями.
 */
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final WorkspaceRepository workspaceRepository;
    private final BookingMapper bookingMapper;
    private final WorkspaceMapper workspaceMapper;
    private final ConferenceRoomMapper conferenceRoomMapper;

    @Override
    public void bookResource(Long userId, Long resourceId, LocalDate startTime, LocalDate endTime) {
        LocalDateTime startDateTime = startTime.atStartOfDay();
        LocalDateTime endDateTime = endTime.atStartOfDay();

        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        Booking booking = new Booking(null, userId, resourceId, startDateTime, endDateTime);
        if (bookingRepository.isConflict(booking)) {
            throw new IllegalArgumentException("Booking conflict detected");
        }

        bookingRepository.save(booking);
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getFilteredBookings(Optional<LocalDate> date, Optional<Long> userId, Optional<Long> resourceId) {
        return bookingRepository.findAll().stream()
                .filter(booking ->
                        (!date.isPresent() || booking.getStartTime().toLocalDate().equals(date.get())) &&
                                (!userId.isPresent() || booking.getUserId().equals(userId.get())) &&
                                (!resourceId.isPresent() || booking.getResourceId().equals(resourceId.get()))
                )
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getUserId().equals(userId))
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.delete(bookingId);
    }

    @Override
    public List<WorkspaceDto> getAvailableWorkspaces(LocalDate date) {
        List<Long> bookedResourceIds = bookingRepository.findAll().stream()
                .filter(booking -> booking.getStartTime().toLocalDate().equals(date))
                .map(Booking::getResourceId)
                .collect(Collectors.toList());

        return workspaceRepository.findAll().stream()
                .filter(workspace -> !bookedResourceIds.contains(workspace.getId()))
                .map(workspaceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConferenceRoomDto> getAvailableConferenceRooms(LocalDate date) {
        List<Long> bookedResourceIds = bookingRepository.findAll().stream()
                .filter(booking -> booking.getStartTime().toLocalDate().equals(date))
                .map(Booking::getResourceId)
                .collect(Collectors.toList());

        return conferenceRoomRepository.findAll().stream()
                .filter(conferenceRoom -> !bookedResourceIds.contains(conferenceRoom.getId()))
                .map(conferenceRoomMapper::toDto)
                .collect(Collectors.toList());
    }
}

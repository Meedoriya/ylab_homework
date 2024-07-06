package org.alibi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.alibi.application.BookingService;
import org.alibi.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookingServletTest {

    private BookingService bookingService;
    private ObjectMapper objectMapper;
    private Validator validator;
    private BookingServlet bookingServlet;

    @BeforeEach
    void setUp() {
        bookingService = mock(BookingService.class);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Регистрация JavaTimeModule
        validator = mock(Validator.class);
        bookingServlet = new BookingServlet(bookingService, objectMapper, validator);
    }

    @Test
    void doGet_ShouldReturnAvailableWorkspaces() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn("/available-workspaces");
        when(request.getParameter("date")).thenReturn("2024-07-10");

        List<WorkspaceDto> workspaces = Collections.singletonList(new WorkspaceDto(1L, "Workspace 1", true));
        when(bookingService.getAvailableWorkspaces(LocalDate.parse("2024-07-10"))).thenReturn(workspaces);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        bookingServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertThat(stringWriter.toString()).contains("Workspace 1");
    }

    @Test
    void doGet_ShouldReturnAvailableConferenceRooms() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn("/available-conference-rooms");
        when(request.getParameter("date")).thenReturn("2024-07-10");

        List<ConferenceRoomDto> conferenceRooms = Collections.singletonList(new ConferenceRoomDto(1L, "Conference Room 1", true));
        when(bookingService.getAvailableConferenceRooms(LocalDate.parse("2024-07-10"))).thenReturn(conferenceRooms);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        bookingServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertThat(stringWriter.toString()).contains("Conference Room 1");
    }

    @Test
    void doGet_ShouldReturnAllBookings() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn(null);

        List<BookingDto> bookings = Collections.singletonList(new BookingDto(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        when(bookingService.getAllBookings()).thenReturn(bookings);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        bookingServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertThat(stringWriter.toString()).contains("\"id\":1");
    }

    @Test
    void doPost_ShouldCreateBooking() throws Exception {
        BookingDto bookingDto = new BookingDto(1L, 1L, 1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2));
        String bookingJson = objectMapper.writeValueAsString(bookingDto);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bookingJson)));

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        when(validator.validate(any(BookingDto.class))).thenReturn(Collections.emptySet());

        bookingServlet.doPost(request, response);

        verify(bookingService, times(1)).bookResource(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void doPost_ShouldReturnValidationErrors() throws Exception {
        BookingDto bookingDto = new BookingDto(null, null, null, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).minusHours(2));
        String bookingJson = objectMapper.writeValueAsString(bookingDto);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bookingJson)));

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        Set<ConstraintViolation<BookingDto>> violations = Collections.singleton(new SimpleConstraintViolation("Validation error"));
        when(validator.validate(any(BookingDto.class))).thenReturn(violations);

        bookingServlet.doPost(request, response);

        verify(bookingService, never()).bookResource(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // Проверка сообщения об ошибках валидации
        String errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        assertThat(stringWriter.toString()).contains(errorMessages);
    }

    @Test
    void doDelete_ShouldCancelBooking() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn("/1");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        bookingServlet.doDelete(request, response);

        verify(bookingService, times(1)).cancelBooking(any(UserDto.class), eq(1L));
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDelete_ShouldReturnBadRequestIfNoBookingId() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        bookingServlet.doDelete(request, response);

        verify(bookingService, never()).cancelBooking(any(UserDto.class), anyLong());
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertThat(stringWriter.toString()).contains("Booking ID is required");
    }

    @Test
    void doDelete_ShouldReturnNotFoundIfBookingDoesNotExist() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn("/999");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        doThrow(new IllegalArgumentException("Booking not found")).when(bookingService).cancelBooking(any(UserDto.class), eq(999L));

        bookingServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertThat(stringWriter.toString()).contains("Booking not found");
    }

    // Вспомогательный класс для представления ошибок валидации
    private static class SimpleConstraintViolation implements ConstraintViolation<BookingDto> {
        private final String message;

        public SimpleConstraintViolation(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return null;
        }

        @Override
        public BookingDto getRootBean() {
            return null;
        }

        @Override
        public Class<BookingDto> getRootBeanClass() {
            return BookingDto.class;
        }

        @Override
        public Object getLeafBean() {
            return null;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[0];
        }

        @Override
        public Object getExecutableReturnValue() {
            return null;
        }

        @Override
        public Object getInvalidValue() {
            return null;
        }

        @Override
        public javax.validation.Path getPropertyPath() {
            return null;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public <U> U unwrap(Class<U> type) {
            return null;
        }
    }
}

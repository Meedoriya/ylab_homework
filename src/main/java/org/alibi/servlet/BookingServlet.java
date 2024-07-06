package org.alibi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alibi.application.BookingService;
import org.alibi.dto.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

/**
 * Сервлет для управления бронированиями.
 */
@WebServlet("/bookings/*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingServlet extends HttpServlet {

    BookingService bookingService;
    ObjectMapper objectMapper;
    Validator validator;

    /**
     * Обрабатывает GET-запросы для получения всех бронирований или доступных рабочих пространств и конференц-залов.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path != null && path.equals("/available-workspaces")) {
            handleGetAvailableWorkspaces(req, resp);
        } else if (path != null && path.equals("/available-conference-rooms")) {
            handleGetAvailableConferenceRooms(req, resp);
        } else {
            List<BookingDto> bookings = bookingService.getAllBookings();
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), bookings);
        }
    }

    private void handleGetAvailableWorkspaces(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String dateStr = req.getParameter("date");
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        List<WorkspaceDto> workspaces = bookingService.getAvailableWorkspaces(date);
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), workspaces);
    }

    private void handleGetAvailableConferenceRooms(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String dateStr = req.getParameter("date");
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        List<ConferenceRoomDto> conferenceRooms = bookingService.getAvailableConferenceRooms(date);
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), conferenceRooms);
    }

    /**
     * Обрабатывает POST-запросы для создания нового бронирования.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookingDto bookingDto = objectMapper.readValue(req.getReader(), BookingDto.class);

        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);
        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), violations);
            return;
        }

        bookingService.bookResource(bookingDto.getUserId(), bookingDto.getResourceId(),
                bookingDto.getStartTime(), bookingDto.getEndTime());
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * Обрабатывает DELETE-запросы для удаления бронирования.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Booking ID is required"));
            return;
        }

        try {
            Long id = Long.valueOf(path.substring(1));
            bookingService.cancelBooking(new UserDto(1L, "admin"), id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse(e.getMessage()));
        }
    }
}

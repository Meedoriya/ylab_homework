package org.alibi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alibi.application.ConferenceRoomService;
import org.alibi.dto.ConferenceRoomDto;
import org.alibi.dto.ErrorResponse;
import org.alibi.dto.UserDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервлет для управления конференц-залами.
 */
@WebServlet("/conference-rooms/*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ConferenceRoomServlet extends HttpServlet {

    ConferenceRoomService conferenceRoomService;
    ObjectMapper objectMapper;
    Validator validator;

    /**
     * Обрабатывает GET-запросы для получения всех конференц-залов.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var conferenceRooms = conferenceRoomService.getAllConferenceRooms();
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), conferenceRooms);
    }

    /**
     * Обрабатывает POST-запросы для создания нового конференц-зала.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var conferenceRoomDto = objectMapper.readValue(req.getReader(), ConferenceRoomDto.class);
        var violations = validator.validate(conferenceRoomDto);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            var errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(","));
            System.out.println("Validation errors: " + errorMessages);
            resp.getWriter().write("{\"errors\":\"" + errorMessages + "\"}");
            return;
        }

        var userDto = new UserDto(1L, "admin");
        conferenceRoomService.addConferenceRoom(userDto, conferenceRoomDto);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * Обрабатывает PUT-запросы для обновления конференц-зала.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var conferenceRoomDto = objectMapper.readValue(req.getReader(), ConferenceRoomDto.class);
        var violations = validator.validate(conferenceRoomDto);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), violations);
            return;
        }

        conferenceRoomService.updateConferenceRoom(new UserDto(1L, "admin"), conferenceRoomDto);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Обрабатывает DELETE-запросы для удаления конференц-зала.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Conference Room ID is required"));
            return;
        }

        try {
            var id = Long.valueOf(path.substring(1));
            conferenceRoomService.deleteConferenceRoom(new UserDto(1L, "admin"), id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse(e.getMessage()));
        }
    }
}

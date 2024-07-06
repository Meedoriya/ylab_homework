package org.alibi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alibi.application.WorkspaceService;
import org.alibi.dto.ErrorResponse;
import org.alibi.dto.UserDto;
import org.alibi.dto.WorkspaceDto;
import org.alibi.dto.ValidationErrorResponse;

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
 * Сервлет для управления рабочими пространствами.
 */
@WebServlet("/workspaces/*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WorkspaceServlet extends HttpServlet {

    WorkspaceService workspaceService;
    ObjectMapper objectMapper;
    Validator validator;

    /**
     * Обрабатывает GET-запросы для получения всех рабочих пространств.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var workspaces = workspaceService.getAllWorkspaces();
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), workspaces);
    }

    /**
     * Обрабатывает POST-запросы для создания нового рабочего пространства.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var workspaceDto = objectMapper.readValue(req.getReader(), WorkspaceDto.class);

        var violations = validator.validate(workspaceDto);
        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            var errorResponse = new ValidationErrorResponse(
                    violations.stream()
                            .map(violation -> new ValidationErrorResponse.ValidationError(
                                    violation.getPropertyPath().toString(), violation.getMessage()))
                            .collect(Collectors.toList())
            );
            objectMapper.writeValue(resp.getWriter(), errorResponse);
            return;
        }

        workspaceService.addWorkspace(new UserDto(1L, "admin"), workspaceDto);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * Обрабатывает PUT-запросы для обновления рабочего пространства.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var workspaceDto = objectMapper.readValue(req.getReader(), WorkspaceDto.class);

        var violations = validator.validate(workspaceDto);
        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            var errorResponse = new ValidationErrorResponse(
                    violations.stream()
                            .map(violation -> new ValidationErrorResponse.ValidationError(
                                    violation.getPropertyPath().toString(), violation.getMessage()))
                            .collect(Collectors.toList())
            );
            objectMapper.writeValue(resp.getWriter(), errorResponse);
            return;
        }

        workspaceService.updateWorkspace(new UserDto(1L, "admin"), workspaceDto);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Обрабатывает DELETE-запросы для удаления рабочего пространства.
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
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Workspace ID is required"));
            return;
        }

        try {
            var id = Long.valueOf(path.substring(1));
            workspaceService.deleteWorkspace(new UserDto(1L, "admin"), id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse(e.getMessage()));
        }
    }
}

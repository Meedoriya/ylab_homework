package org.alibi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alibi.application.UserService;
import org.alibi.dto.ErrorResponse;
import org.alibi.dto.UserDto;
import org.alibi.dto.UserRegistrationDto;
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
 * Сервлет для управления пользователями.
 */
@WebServlet("/users/*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServlet extends HttpServlet {

    UserService userService;
    ObjectMapper objectMapper;
    Validator validator;

    /**
     * Обрабатывает POST-запросы для регистрации и авторизации пользователей.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var path = req.getPathInfo();
        if (path == null || path.equals("/register")) {
            handleRegister(req, resp);
        } else if (path.equals("/login")) {
            handleLogin(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Not found"));
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var userRegistrationDto = objectMapper.readValue(req.getReader(), UserRegistrationDto.class);
        var violations = validator.validate(userRegistrationDto);

        if (!violations.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            var validationErrors = violations.stream()
                    .map(violation -> new ValidationErrorResponse.ValidationError(
                            violation.getPropertyPath().toString(), violation.getMessage()))
                    .collect(Collectors.toList());
            var errorResponse = new ValidationErrorResponse(validationErrors);
            objectMapper.writeValue(resp.getWriter(), errorResponse);
            return;
        }

        userService.registerUser(userRegistrationDto);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UserRegistrationDto userRegistrationDto = objectMapper.readValue(req.getReader(), UserRegistrationDto.class);

            Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(userRegistrationDto);
            if (!violations.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ErrorResponse("Validation failed"));
                return;
            }

            UserDto userDto = userService.loginUser(userRegistrationDto.username(), userRegistrationDto.password())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), userDto);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse(e.getMessage()));
        }
    }
}

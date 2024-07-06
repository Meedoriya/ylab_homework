package org.alibi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alibi.application.UserService;
import org.alibi.dto.ErrorResponse;
import org.alibi.dto.UserDto;
import org.alibi.dto.UserRegistrationDto;
import org.alibi.dto.ValidationErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServletTest {

    private UserService userService;
    private UserServlet userServlet;
    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        objectMapper = new ObjectMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        userServlet = new UserServlet(userService, objectMapper, validator);
    }

    @Test
    @DisplayName("Should register a user")
    void doPost_ShouldRegisterUser() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        String json = "{\"username\":\"testuser\",\"password\":\"password\"}";

        when(request.getPathInfo()).thenReturn("/register");
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        verify(userService).registerUser(any(UserRegistrationDto.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Should login a user")
    void doPost_ShouldLoginUser() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        String json = "{\"username\":\"testuser\",\"password\":\"password\"}";
        UserDto userDto = new UserDto(1L, "testuser");

        when(request.getPathInfo()).thenReturn("/login");
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(userService.loginUser(eq("testuser"), eq("password"))).thenReturn(java.util.Optional.of(userDto));
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(objectMapper.writeValueAsString(userDto), stringWriter.toString().trim());
    }

    @Test
    @DisplayName("Should return error for invalid login")
    void doPost_ShouldReturnErrorForInvalidLogin() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        String json = "{\"username\":\"invaliduser\",\"password\":\"password\"}";

        when(request.getPathInfo()).thenReturn("/login");
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(userService.loginUser(eq("invaliduser"), eq("password"))).thenReturn(java.util.Optional.empty());
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("{\"message\":\"Invalid username or password\"}", stringWriter.toString().trim());
    }

    @Test
    @DisplayName("Should return not found for invalid path")
    void doPost_ShouldReturnNotFoundForInvalidPath() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn("/invalid");
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertEquals("{\"message\":\"Not found\"}", stringWriter.toString().trim());
    }
}

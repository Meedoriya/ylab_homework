package org.alibi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alibi.application.ConferenceRoomService;
import org.alibi.dto.ConferenceRoomDto;
import org.alibi.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
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

class ConferenceRoomServletTest {

    private ConferenceRoomService conferenceRoomService;
    private ConferenceRoomServlet conferenceRoomServlet;
    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        conferenceRoomService = Mockito.mock(ConferenceRoomService.class);
        objectMapper = new ObjectMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        conferenceRoomServlet = new ConferenceRoomServlet(conferenceRoomService, objectMapper, validator);
    }

    @Test
    @DisplayName("Should return all conference rooms")
    void doGet_ShouldReturnAllConferenceRooms() throws Exception {
        var conferenceRooms = List.of(new ConferenceRoomDto(1L, "Conference Room 1", true));
        when(conferenceRoomService.getAllConferenceRooms()).thenReturn(conferenceRooms);

        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var stringWriter = new StringWriter();
        var writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        conferenceRoomServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(objectMapper.writeValueAsString(conferenceRooms), stringWriter.toString().trim());
    }

    @Test
    @DisplayName("Should create a conference room")
    void doPost_ShouldCreateConferenceRoom() throws Exception {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var stringWriter = new StringWriter();
        var writer = new PrintWriter(stringWriter);

        var json = "{\"id\":1,\"name\":\"Conference Room 1\",\"available\":true}";

        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(response.getWriter()).thenReturn(writer);

        conferenceRoomServlet.doPost(request, response);

        verify(conferenceRoomService).addConferenceRoom(any(UserDto.class), any(ConferenceRoomDto.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Should return validation errors for invalid conference room")
    void doPost_ShouldReturnValidationErrors() throws Exception {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var stringWriter = new StringWriter();
        var writer = new PrintWriter(stringWriter);

        var json = "{\"id\":1,\"name\":\"ab\",\"available\":true}"; // Invalid name, less than 3 characters

        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(response.getWriter()).thenReturn(writer);

        conferenceRoomServlet.doPost(request, response);

        // Проверка статуса ответа
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // Проверка содержимого ответа
        String expectedResponse = "{\"errors\":\"размер должен быть между 3 и 50\"}";
        assertEquals(expectedResponse, stringWriter.toString().trim());
    }


    @Test
    @DisplayName("Should update a conference room")
    void doPut_ShouldUpdateConferenceRoom() throws Exception {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var stringWriter = new StringWriter();
        var writer = new PrintWriter(stringWriter);

        var json = "{\"id\":1,\"name\":\"Updated Conference Room\",\"available\":true}";

        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(response.getWriter()).thenReturn(writer);

        conferenceRoomServlet.doPut(request, response);

        verify(conferenceRoomService).updateConferenceRoom(any(UserDto.class), any(ConferenceRoomDto.class));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Should delete a conference room")
    void doDelete_ShouldDeleteConferenceRoom() throws Exception {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/1");

        conferenceRoomServlet.doDelete(request, response);

        verify(conferenceRoomService).deleteConferenceRoom(any(UserDto.class), eq(1L));
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Should return error if conference room ID is not provided")
    void doDelete_ShouldReturnErrorIfConferenceRoomIdNotProvided() throws Exception {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var stringWriter = new StringWriter();
        var writer = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        conferenceRoomServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("{\"message\":\"Conference Room ID is required\"}", stringWriter.toString().trim());
    }
}


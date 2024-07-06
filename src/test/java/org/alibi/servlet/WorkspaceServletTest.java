package org.alibi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alibi.application.WorkspaceService;
import org.alibi.dto.UserDto;
import org.alibi.dto.ValidationErrorResponse;
import org.alibi.dto.WorkspaceDto;
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

class WorkspaceServletTest {

    private WorkspaceService workspaceService;
    private WorkspaceServlet workspaceServlet;
    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        workspaceService = Mockito.mock(WorkspaceService.class);
        objectMapper = new ObjectMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        workspaceServlet = new WorkspaceServlet(workspaceService, objectMapper, validator);
    }

    @Test
    @DisplayName("Should return all workspaces")
    void doGet_ShouldReturnAllWorkspaces() throws Exception {
        List<WorkspaceDto> workspaces = List.of(new WorkspaceDto(1L, "Workspace 1", true));
        when(workspaceService.getAllWorkspaces()).thenReturn(workspaces);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        workspaceServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals(objectMapper.writeValueAsString(workspaces), stringWriter.toString().trim());
    }

    @Test
    @DisplayName("Should create a workspace")
    void doPost_ShouldCreateWorkspace() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        String json = "{\"id\":1,\"name\":\"Workspace 1\",\"available\":true}";

        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(response.getWriter()).thenReturn(writer);

        workspaceServlet.doPost(request, response);

        verify(workspaceService).addWorkspace(any(UserDto.class), any(WorkspaceDto.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Should return validation errors for invalid workspace")
    void doPost_ShouldReturnValidationErrorsForInvalidWorkspace() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        String json = "{\"id\":1,\"name\":\"\",\"available\":true}"; // Invalid name

        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(response.getWriter()).thenReturn(writer);

        workspaceServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        ValidationErrorResponse expectedResponse = new ValidationErrorResponse(
                List.of(new ValidationErrorResponse.ValidationError("name", "Name is required"))
        );
        assertEquals(objectMapper.writeValueAsString(expectedResponse), stringWriter.toString().trim());
    }

    @Test
    @DisplayName("Should update a workspace")
    void doPut_ShouldUpdateWorkspace() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        String json = "{\"id\":1,\"name\":\"Updated Workspace\",\"available\":true}";

        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(response.getWriter()).thenReturn(writer);

        workspaceServlet.doPut(request, response);

        verify(workspaceService).updateWorkspace(any(UserDto.class), any(WorkspaceDto.class));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Should return validation errors for invalid workspace update")
    void doPut_ShouldReturnValidationErrorsForInvalidWorkspaceUpdate() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        String json = "{\"id\":1,\"name\":\"\",\"available\":true}"; // Invalid name

        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(response.getWriter()).thenReturn(writer);

        workspaceServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

        ValidationErrorResponse expectedResponse = new ValidationErrorResponse(
                List.of(new ValidationErrorResponse.ValidationError("name", "Name is required"))
        );
        assertEquals(objectMapper.writeValueAsString(expectedResponse), stringWriter.toString().trim());
    }

    @Test
    @DisplayName("Should delete a workspace")
    void doDelete_ShouldDeleteWorkspace() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/1");

        workspaceServlet.doDelete(request, response);

        verify(workspaceService).deleteWorkspace(any(UserDto.class), eq(1L));
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Should return error if workspace ID is not provided")
    void doDelete_ShouldReturnErrorIfWorkspaceIdNotProvided() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn(null);
        when(response.getWriter()).thenReturn(writer);

        workspaceServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals("{\"message\":\"Workspace ID is required\"}", stringWriter.toString().trim());
    }

    @Test
    @DisplayName("Should return not found for invalid workspace ID")
    void doDelete_ShouldReturnNotFoundForInvalidWorkspaceId() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn("/999");
        doThrow(new IllegalArgumentException("Workspace not found")).when(workspaceService).deleteWorkspace(any(UserDto.class), eq(999L));
        when(response.getWriter()).thenReturn(writer);

        workspaceServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertEquals("{\"message\":\"Workspace not found\"}", stringWriter.toString().trim());
    }
}

package org.alibi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alibi.config.TestConfig;
import org.alibi.domain.dto.WorkspaceDto;
import org.alibi.service.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@EnableWebMvc
public class WorkspaceControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private WorkspaceService workspaceService;

    private MockMvc mockMvc;

    private WorkspaceDto workspaceDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        workspaceDto = new WorkspaceDto(1L, "Workspace 1", true);
    }

    @Test
    @DisplayName("Добавление рабочего места")
    public void testAddWorkspace() throws Exception {
        mockMvc.perform(post("/api/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(workspaceDto)))
                .andExpect(status().isCreated());

        verify(workspaceService, times(1)).addWorkspace(any(WorkspaceDto.class));
    }

    @Test
    @DisplayName("Обновление рабочего места")
    public void testUpdateWorkspace() throws Exception {
        mockMvc.perform(put("/api/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(workspaceDto)))
                .andExpect(status().isOk());

        verify(workspaceService, times(1)).updateWorkspace(any(WorkspaceDto.class));
    }

    @Test
    @DisplayName("Удаление рабочего места")
    public void testDeleteWorkspace() throws Exception {
        mockMvc.perform(delete("/api/workspaces/1"))
                .andExpect(status().isNoContent());

        verify(workspaceService, times(1)).deleteWorkspace(1L);
    }

    @Test
    @DisplayName("Получение всех рабочих мест")
    public void testGetAllWorkspaces() throws Exception {
        when(workspaceService.getAllWorkspaces()).thenReturn(Collections.singletonList(workspaceDto));

        mockMvc.perform(get("/api/workspaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Workspace 1"));

        verify(workspaceService, times(1)).getAllWorkspaces();
    }
}

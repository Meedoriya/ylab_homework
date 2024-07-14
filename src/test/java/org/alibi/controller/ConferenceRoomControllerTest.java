package org.alibi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alibi.config.TestConfig;
import org.alibi.domain.dto.ConferenceRoomDto;
import org.alibi.service.ConferenceRoomService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@EnableWebMvc
public class ConferenceRoomControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ConferenceRoomService conferenceRoomService;

    private MockMvc mockMvc;

    private ConferenceRoomDto conferenceRoomDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        conferenceRoomDto = new ConferenceRoomDto(1L, "Conference Room 1", true);
    }

    @Test
    @DisplayName("Добавление нового конференц-зала")
    public void testAddConferenceRoom() throws Exception {
        mockMvc.perform(post("/api/conference-rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(conferenceRoomDto)))
                .andExpect(status().isCreated());

        verify(conferenceRoomService, times(1)).addConferenceRoom(any(ConferenceRoomDto.class));
    }

    @Test
    @DisplayName("Получение всех конференц-залов")
    public void testGetAllConferenceRooms() throws Exception {
        when(conferenceRoomService.getAllConferenceRooms()).thenReturn(Collections.singletonList(conferenceRoomDto));

        mockMvc.perform(get("/api/conference-rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Conference Room 1"));

        verify(conferenceRoomService, times(1)).getAllConferenceRooms();
    }

    @Test
    @DisplayName("Получение конференц-зала по ID")
    public void testGetConferenceRoomById() throws Exception {
        when(conferenceRoomService.getConferenceRoomById(1L)).thenReturn(Optional.of(conferenceRoomDto));

        mockMvc.perform(get("/api/conference-rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Conference Room 1"));

        verify(conferenceRoomService, times(1)).getConferenceRoomById(1L);
    }

    @Test
    @DisplayName("Удаление конференц-зала")
    public void testDeleteConferenceRoom() throws Exception {
        mockMvc.perform(delete("/api/conference-rooms/1"))
                .andExpect(status().isNoContent());

        verify(conferenceRoomService, times(1)).deleteConferenceRoom(1L);
    }
}

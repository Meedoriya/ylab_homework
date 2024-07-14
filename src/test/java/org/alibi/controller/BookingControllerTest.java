package org.alibi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alibi.config.TestConfig;
import org.alibi.domain.dto.BookingDto;
import org.alibi.domain.dto.ConferenceRoomDto;
import org.alibi.domain.dto.WorkspaceDto;
import org.alibi.service.BookingService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@EnableWebMvc
public class BookingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BookingService bookingService;

    private MockMvc mockMvc;

    private BookingDto bookingDto;
    private WorkspaceDto workspaceDto;
    private ConferenceRoomDto conferenceRoomDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        bookingDto = new BookingDto(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        workspaceDto = new WorkspaceDto(1L, "Workspace 1", true);
        conferenceRoomDto = new ConferenceRoomDto(1L, "Conference Room 1", true);
    }

    @Test
    @DisplayName("Создание нового бронирования")
    public void testBookResource() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookingDto)))
                .andExpect(status().isCreated());

        verify(bookingService, times(1)).bookResource(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("Получение всех бронирований")
    public void testGetAllBookings() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L));

        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    @DisplayName("Получение отфильтрованных бронирований")
    public void testGetFilteredBookings() throws Exception {
        when(bookingService.getFilteredBookings(any(), any(), any())).thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/api/bookings/filter")
                        .param("date", "2024-07-12")
                        .param("userId", "1")
                        .param("resourceId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L));

        verify(bookingService, times(1)).getFilteredBookings(any(), any(), any());
    }

    @Test
    @DisplayName("Получение бронирований пользователя")
    public void testGetUserBookings() throws Exception {
        when(bookingService.getUserBookings(1L)).thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/api/bookings/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L));

        verify(bookingService, times(1)).getUserBookings(1L);
    }

    @Test
    @DisplayName("Отмена бронирования")
    public void testCancelBooking() throws Exception {
        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());

        verify(bookingService, times(1)).cancelBooking(1L);
    }

    @Test
    @DisplayName("Получение доступных рабочих мест")
    public void testGetAvailableWorkspaces() throws Exception {
        when(bookingService.getAvailableWorkspaces(any(LocalDate.class))).thenReturn(Collections.singletonList(workspaceDto));

        mockMvc.perform(get("/api/bookings/available-workspaces")
                        .param("date", "2024-07-12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Workspace 1"));

        verify(bookingService, times(1)).getAvailableWorkspaces(any(LocalDate.class));
    }

    @Test
    @DisplayName("Получение доступных конференц-залов")
    public void testGetAvailableConferenceRooms() throws Exception {
        when(bookingService.getAvailableConferenceRooms(any(LocalDate.class))).thenReturn(Collections.singletonList(conferenceRoomDto));

        mockMvc.perform(get("/api/bookings/available-conference-rooms")
                        .param("date", "2024-07-12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Conference Room 1"));

        verify(bookingService, times(1)).getAvailableConferenceRooms(any(LocalDate.class));
    }
}

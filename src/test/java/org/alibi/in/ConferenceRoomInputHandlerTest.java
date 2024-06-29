package org.alibi.in;

import org.alibi.application.ConferenceRoomService;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.User;
import org.alibi.out.ConsoleOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConferenceRoomInputHandlerTest {

    @Mock
    private ConferenceRoomService conferenceRoomService;

    @Mock
    private ConsoleOutput consoleOutput;

    @InjectMocks
    private ConferenceRoomInputHandler conferenceRoomInputHandler;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    @DisplayName("Should successfully add conference room")
    void testAddConferenceRoom() {
        String input = "Conference Room 1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        conferenceRoomInputHandler = new ConferenceRoomInputHandler(conferenceRoomService, consoleOutput);
        conferenceRoomInputHandler.addConferenceRoom(user);

        ArgumentCaptor<ConferenceRoom> conferenceRoomCaptor = ArgumentCaptor.forClass(ConferenceRoom.class);
        verify(conferenceRoomService, times(1)).addConferenceRoom(eq(user), conferenceRoomCaptor.capture());
        ConferenceRoom capturedConferenceRoom = conferenceRoomCaptor.getValue();

        assertThat(capturedConferenceRoom.getName()).isEqualTo("Conference Room 1");
    }

    @Test
    @DisplayName("Should successfully update conference room")
    void testUpdateConferenceRoom() {
        String input = "1\nUpdated Conference Room\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        conferenceRoomInputHandler = new ConferenceRoomInputHandler(conferenceRoomService, consoleOutput);
        conferenceRoomInputHandler.updateConferenceRoom(user);

        ArgumentCaptor<ConferenceRoom> conferenceRoomCaptor = ArgumentCaptor.forClass(ConferenceRoom.class);
        verify(conferenceRoomService, times(1)).updateConferenceRoom(eq(user), conferenceRoomCaptor.capture());
        ConferenceRoom capturedConferenceRoom = conferenceRoomCaptor.getValue();

        assertThat(capturedConferenceRoom.getId()).isEqualTo(1L);
        assertThat(capturedConferenceRoom.getName()).isEqualTo("Updated Conference Room");
    }

    @Test
    @DisplayName("Should successfully delete conference room")
    void testDeleteConferenceRoom() {
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        conferenceRoomInputHandler = new ConferenceRoomInputHandler(conferenceRoomService, consoleOutput);
        conferenceRoomInputHandler.deleteConferenceRoom(user);

        verify(conferenceRoomService, times(1)).deleteConferenceRoom(eq(user), eq(1L));
    }

    @Test
    @DisplayName("Should display all conference rooms")
    void testViewAllConferenceRooms() {
        when(conferenceRoomService.getAllConferenceRooms()).thenReturn(Collections.singletonList(new ConferenceRoom(1L, "Conference Room 1", true)));

        conferenceRoomInputHandler.viewAllConferenceRooms();

        verify(consoleOutput, times(1)).printConferenceRooms(anyList());
    }
}

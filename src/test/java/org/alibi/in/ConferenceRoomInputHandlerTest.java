package org.alibi.in;

import org.alibi.application.ConferenceRoomService;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.User;
import org.alibi.out.ConsoleOutput;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ConferenceRoomInputHandlerTest {

    private ConferenceRoomService conferenceRoomService;
    private ConsoleOutput consoleOutput;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        conferenceRoomService = mock(ConferenceRoomService.class);
        consoleOutput = mock(ConsoleOutput.class);
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should add conference room successfully")
    void testAddConferenceRoom() {
        String input = "Conference Room 1\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        ConferenceRoomInputHandler conferenceRoomInputHandler = new ConferenceRoomInputHandler(conferenceRoomService, consoleOutput);
        User user = new User();
        user.setId(1L);

        conferenceRoomInputHandler.addConferenceRoom(user);

        verify(conferenceRoomService).addConferenceRoom(eq(user), any(ConferenceRoom.class));
        assertThat(outContent.toString()).contains("Conference room added successfully.");
    }

    @Test
    @DisplayName("Should update conference room successfully")
    void testUpdateConferenceRoom() {
        String input = "1\nConference Room Updated\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        ConferenceRoomInputHandler conferenceRoomInputHandler = new ConferenceRoomInputHandler(conferenceRoomService, consoleOutput);
        User user = new User();
        user.setId(1L);

        conferenceRoomInputHandler.updateConferenceRoom(user);

        verify(conferenceRoomService).updateConferenceRoom(eq(user), any(ConferenceRoom.class));
        assertThat(outContent.toString()).contains("Conference room updated successfully.");
    }

    @Test
    @DisplayName("Should delete conference room successfully")
    void testDeleteConferenceRoom() {
        String input = "1\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        ConferenceRoomInputHandler conferenceRoomInputHandler = new ConferenceRoomInputHandler(conferenceRoomService, consoleOutput);
        User user = new User();
        user.setId(1L);

        conferenceRoomInputHandler.deleteConferenceRoom(user);

        verify(conferenceRoomService).deleteConferenceRoom(eq(user), eq(1L));
        assertThat(outContent.toString()).contains("Conference room deleted successfully.");
    }

    @Test
    @DisplayName("Should view all conference rooms")
    void testViewAllConferenceRooms() {
        List<ConferenceRoom> conferenceRooms = new ArrayList<>();
        conferenceRooms.add(new ConferenceRoom(1L, "Conference Room 1", true));
        conferenceRooms.add(new ConferenceRoom(2L, "Conference Room 2", true));

        when(conferenceRoomService.getAllConferenceRooms()).thenReturn(conferenceRooms);

        ConferenceRoomInputHandler conferenceRoomInputHandler = new ConferenceRoomInputHandler(conferenceRoomService, consoleOutput);
        conferenceRoomInputHandler.viewAllConferenceRooms();

        verify(consoleOutput).printConferenceRooms(eq(conferenceRooms));
    }
}

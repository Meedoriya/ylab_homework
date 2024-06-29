package org.alibi.application;

import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.User;
import org.alibi.domain.repository.ConferenceRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ConferenceRoomServiceTest {

    private ConferenceRoomRepository conferenceRoomRepository;
    private ConferenceRoomService conferenceRoomService;
    private User mockUser;

    @BeforeEach
    void setUp() {
        conferenceRoomRepository = Mockito.mock(ConferenceRoomRepository.class);
        conferenceRoomService = new ConferenceRoomService(conferenceRoomRepository);
        mockUser = new User(1L, "admin", "password"); // Создаем тестового пользователя
    }

    @Test
    @DisplayName("Should add conference room")
    void shouldAddConferenceRoom() {
        ConferenceRoom conferenceRoom = new ConferenceRoom(1L, "Conference Room 1", true);

        conferenceRoomService.addConferenceRoom(mockUser, conferenceRoom);

        Mockito.verify(conferenceRoomRepository).save(conferenceRoom);
    }

    @Test
    @DisplayName("Should update conference room")
    void shouldUpdateConferenceRoom() {
        ConferenceRoom conferenceRoom = new ConferenceRoom(1L, "Conference Room 1", true);

        conferenceRoomService.updateConferenceRoom(mockUser, conferenceRoom);

        Mockito.verify(conferenceRoomRepository).update(conferenceRoom);
    }

    @Test
    @DisplayName("Should delete conference room")
    void shouldDeleteConferenceRoom() {
        Long conferenceRoomId = 1L;

        conferenceRoomService.deleteConferenceRoom(mockUser, conferenceRoomId);

        Mockito.verify(conferenceRoomRepository).delete(conferenceRoomId);
    }

    @Test
    @DisplayName("Should get all conference rooms")
    void shouldGetAllConferenceRooms() {
        ConferenceRoom conferenceRoom = new ConferenceRoom(1L, "Conference Room 1", true);
        when(conferenceRoomRepository.findAll()).thenReturn(List.of(conferenceRoom));

        List<ConferenceRoom> conferenceRooms = conferenceRoomService.getAllConferenceRooms();

        assertThat(conferenceRooms).contains(conferenceRoom);
    }
}

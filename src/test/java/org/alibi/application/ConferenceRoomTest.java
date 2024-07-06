package org.alibi.application;

import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.repository.ConferenceRoomRepository;
import org.alibi.dto.ConferenceRoomDto;
import org.alibi.dto.UserDto;
import org.alibi.mapper.ConferenceRoomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConferenceRoomServiceTest {

    private ConferenceRoomRepository conferenceRoomRepository;
    private ConferenceRoomService conferenceRoomService;
    private ConferenceRoomMapper conferenceRoomMapper = ConferenceRoomMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        conferenceRoomRepository = mock(ConferenceRoomRepository.class);
        conferenceRoomService = new ConferenceRoomService(conferenceRoomRepository);
    }

    @Test
    @DisplayName("Should add conference room successfully")
    void addConferenceRoom() {
        UserDto userDto = new UserDto(1L, "admin");
        ConferenceRoomDto conferenceRoomDto = new ConferenceRoomDto(1L, "Conference Room 1", true);

        conferenceRoomService.addConferenceRoom(userDto, conferenceRoomDto);

        verify(conferenceRoomRepository, times(1)).save(any(ConferenceRoom.class));
    }

    @Test
    @DisplayName("Should throw exception when adding conference room with non-registered user")
    void addConferenceRoom_NotRegisteredUser() {
        UserDto userDto = null;  // Non-registered user
        ConferenceRoomDto conferenceRoomDto = new ConferenceRoomDto(1L, "Conference Room 1", true);

        assertThrows(SecurityException.class, () -> conferenceRoomService.addConferenceRoom(userDto, conferenceRoomDto));
    }

    @Test
    @DisplayName("Should update conference room successfully")
    void updateConferenceRoom() {
        UserDto userDto = new UserDto(1L, "admin");
        ConferenceRoomDto conferenceRoomDto = new ConferenceRoomDto(1L, "Conference Room 1", true);

        conferenceRoomService.updateConferenceRoom(userDto, conferenceRoomDto);

        verify(conferenceRoomRepository, times(1)).update(any(ConferenceRoom.class));
    }

    @Test
    @DisplayName("Should throw exception when updating conference room with non-registered user")
    void updateConferenceRoom_NotRegisteredUser() {
        UserDto userDto = null;  // Non-registered user
        ConferenceRoomDto conferenceRoomDto = new ConferenceRoomDto(1L, "Conference Room 1", true);

        assertThrows(SecurityException.class, () -> conferenceRoomService.updateConferenceRoom(userDto, conferenceRoomDto));
    }

    @Test
    @DisplayName("Should delete conference room successfully")
    void deleteConferenceRoom() {
        UserDto userDto = new UserDto(1L, "admin");
        Long conferenceRoomId = 1L;

        conferenceRoomService.deleteConferenceRoom(userDto, conferenceRoomId);

        verify(conferenceRoomRepository, times(1)).delete(conferenceRoomId);
    }

    @Test
    @DisplayName("Should throw exception when deleting conference room with non-registered user")
    void deleteConferenceRoom_NotRegisteredUser() {
        UserDto userDto = null;  // Non-registered user
        Long conferenceRoomId = 1L;

        assertThrows(SecurityException.class, () -> conferenceRoomService.deleteConferenceRoom(userDto, conferenceRoomId));
    }

    @Test
    @DisplayName("Should get all conference rooms successfully")
    void getAllConferenceRooms() {
        List<ConferenceRoom> conferenceRooms = List.of(new ConferenceRoom(1L, "Conference Room 1", true));
        when(conferenceRoomRepository.findAll()).thenReturn(conferenceRooms);

        List<ConferenceRoomDto> conferenceRoomDtos = conferenceRoomService.getAllConferenceRooms();

        assertThat(conferenceRoomDtos).hasSize(1);
        assertThat(conferenceRoomDtos.get(0).getName()).isEqualTo("Conference Room 1");
    }
}

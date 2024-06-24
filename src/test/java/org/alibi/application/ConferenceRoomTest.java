package org.alibi.application;

import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.Role;
import org.alibi.domain.model.User;
import org.alibi.domain.repository.ConferenceRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ConferenceRoomServiceTest {

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private ConferenceRoomService conferenceRoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should throw exception when user is not admin")
    void addConferenceRoomShouldThrowExceptionWhenUserIsNotAdmin() {
        User user = new User();
        user.setRole(Role.valueOf("USER"));

        ConferenceRoom conferenceRoom = new ConferenceRoom();

        when(authorizationService.isAdmin(user)).thenReturn(false);

        assertThatThrownBy(() -> conferenceRoomService.addConferenceRoom(user, conferenceRoom))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Only admin can add conference rooms.");
    }

    @Test
    @DisplayName("Should save conference room when user is admin")
    void addConferenceRoomShouldSaveConferenceRoomWhenUserIsAdmin() {
        User user = new User();
        user.setRole(Role.valueOf("ADMIN"));

        ConferenceRoom conferenceRoom = new ConferenceRoom();

        when(authorizationService.isAdmin(user)).thenReturn(true);

        conferenceRoomService.addConferenceRoom(user, conferenceRoom);

        verify(conferenceRoomRepository, times(1)).save(conferenceRoom);
    }

    @Test
    @DisplayName("Should throw exception when user is not admin")
    void updateConferenceRoomShouldThrowExceptionWhenUserIsNotAdmin() {
        User user = new User();
        user.setRole(Role.valueOf("USER"));

        ConferenceRoom conferenceRoom = new ConferenceRoom();

        when(authorizationService.isAdmin(user)).thenReturn(false);

        assertThatThrownBy(() -> conferenceRoomService.updateConferenceRoom(user, conferenceRoom))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Only admin can update conference rooms.");
    }

    @Test
    @DisplayName("Should update conference room when user is admin")
    void updateConferenceRoomShouldUpdateConferenceRoomWhenUserIsAdmin() {
        User user = new User();
        user.setRole(Role.valueOf("ADMIN"));

        ConferenceRoom conferenceRoom = new ConferenceRoom();

        when(authorizationService.isAdmin(user)).thenReturn(true);

        conferenceRoomService.updateConferenceRoom(user, conferenceRoom);

        verify(conferenceRoomRepository, times(1)).update(conferenceRoom);
    }

    @Test
    @DisplayName("Should throw exception when user is not admin")
    void deleteConferenceRoomShouldThrowExceptionWhenUserIsNotAdmin() {
        User user = new User();
        user.setRole(Role.valueOf("USER"));

        Long conferenceRoomId = 1L;

        when(authorizationService.isAdmin(user)).thenReturn(false);

        assertThatThrownBy(() -> conferenceRoomService.deleteConferenceRoom(user, conferenceRoomId))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Only admin can delete conference rooms.");
    }

    @Test
    @DisplayName("Should delete conference room when user is admin")
    void deleteConferenceRoomShouldDeleteConferenceRoomWhenUserIsAdmin() {
        User user = new User();
        user.setRole(Role.valueOf("ADMIN"));

        Long conferenceRoomId = 1L;

        when(authorizationService.isAdmin(user)).thenReturn(true);

        conferenceRoomService.deleteConferenceRoom(user, conferenceRoomId);

        verify(conferenceRoomRepository, times(1)).delete(conferenceRoomId);
    }
}

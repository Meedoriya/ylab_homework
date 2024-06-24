package org.alibi.in;

import org.alibi.application.ConferenceRoomService;
import org.alibi.application.UserService;
import org.alibi.application.WorkspaceService;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.Role;
import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class PrepopulateDataTest {

    private UserService userService;
    private WorkspaceService workspaceService;
    private ConferenceRoomService conferenceRoomService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        workspaceService = mock(WorkspaceService.class);
        conferenceRoomService = mock(ConferenceRoomService.class);
    }

    @Test
    @DisplayName("Should prepopulate data")
    void testPrepopulate() {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setPassword("adminPass");
        adminUser.setRole(Role.ADMIN);

        when(userService.loginUser("admin", "adminPass")).thenReturn(adminUser);

        PrepopulateData.prepopulate(userService, workspaceService, conferenceRoomService);

        verify(userService, times(1)).registerUser("admin", "adminPass", Role.ADMIN);
        verify(userService, times(1)).registerUser("user", "userPass", Role.USER);
        verify(userService, times(1)).loginUser("admin", "adminPass");

        verify(workspaceService, times(1)).addWorkspace(adminUser, new Workspace(1L, "Workspace 1", true));
        verify(workspaceService, times(1)).addWorkspace(adminUser, new Workspace(2L, "Workspace 2", true));

        verify(conferenceRoomService, times(1)).addConferenceRoom(adminUser, new ConferenceRoom(1L, "Conference Room 1", true));
        verify(conferenceRoomService, times(1)).addConferenceRoom(adminUser, new ConferenceRoom(2L, "Conference Room 2", true));
    }
}


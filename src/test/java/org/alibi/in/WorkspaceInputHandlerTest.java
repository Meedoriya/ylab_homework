package org.alibi.in;

import org.alibi.application.WorkspaceService;
import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;
import org.alibi.out.ConsoleOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class WorkspaceInputHandlerTest {

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private ConsoleOutput consoleOutput;

    @InjectMocks
    private WorkspaceInputHandler workspaceInputHandler;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
    }

    @Test
    @DisplayName("Should add workspace")
    void testAddWorkspace() {
        String input = "New Workspace\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        workspaceInputHandler = new WorkspaceInputHandler(workspaceService, scanner, consoleOutput);

        workspaceInputHandler.addWorkspace(user);

        verify(workspaceService).addWorkspace(any(User.class), any(Workspace.class));
    }

    @Test
    @DisplayName("Should update workspace")
    void testUpdateWorkspace() {
        String input = "1\nUpdated Workspace\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        workspaceInputHandler = new WorkspaceInputHandler(workspaceService, scanner, consoleOutput);

        workspaceInputHandler.updateWorkspace(user);

        verify(workspaceService).updateWorkspace(any(User.class), any(Workspace.class));
    }

    @Test
    @DisplayName("Should delete workspace")
    void testDeleteWorkspace() {
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        workspaceInputHandler = new WorkspaceInputHandler(workspaceService, scanner, consoleOutput);

        workspaceInputHandler.deleteWorkspace(user);

        verify(workspaceService).deleteWorkspace(any(User.class), anyLong());
    }

    @Test
    @DisplayName("Should view all workspaces")
    void testViewAllWorkspaces() {
        workspaceInputHandler.viewAllWorkspaces();

        verify(consoleOutput).printWorkspaces(anyList());
    }
}

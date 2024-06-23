package org.alibi.in;

import org.alibi.application.WorkspaceService;
import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;
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

class WorkspaceInputHandlerTest {

    private WorkspaceService workspaceService;
    private ConsoleOutput consoleOutput;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        workspaceService = mock(WorkspaceService.class);
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
    void testAddWorkspace() {
        String input = "Workspace 1\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        WorkspaceInputHandler workspaceInputHandler = new WorkspaceInputHandler(workspaceService, consoleOutput);
        User user = new User();
        user.setId(1L);

        workspaceInputHandler.addWorkspace(user);

        verify(workspaceService).addWorkspace(eq(user), any(Workspace.class));
        assertThat(outContent.toString()).contains("Workspace added successfully.");
    }

    @Test
    void testUpdateWorkspace() {
        String input = "1\nWorkspace Updated\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        WorkspaceInputHandler workspaceInputHandler = new WorkspaceInputHandler(workspaceService, consoleOutput);
        User user = new User();
        user.setId(1L);

        workspaceInputHandler.updateWorkspace(user);

        verify(workspaceService).updateWorkspace(eq(user), any(Workspace.class));
        assertThat(outContent.toString()).contains("Workspace updated successfully.");
    }

    @Test
    void testDeleteWorkspace() {
        String input = "1\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        WorkspaceInputHandler workspaceInputHandler = new WorkspaceInputHandler(workspaceService, consoleOutput);
        User user = new User();
        user.setId(1L);

        workspaceInputHandler.deleteWorkspace(user);

        verify(workspaceService).deleteWorkspace(eq(user), eq(1L));
        assertThat(outContent.toString()).contains("Workspace deleted successfully.");
    }

    @Test
    void testViewAllWorkspaces() {
        List<Workspace> workspaces = new ArrayList<>();
        workspaces.add(new Workspace(1L, "Workspace 1", true));
        workspaces.add(new Workspace(2L, "Workspace 2", true));

        when(workspaceService.getAllWorkspaces()).thenReturn(workspaces);

        WorkspaceInputHandler workspaceInputHandler = new WorkspaceInputHandler(workspaceService, consoleOutput);
        workspaceInputHandler.viewAllWorkspaces();

        verify(consoleOutput).printWorkspaces(eq(workspaces));
    }
}

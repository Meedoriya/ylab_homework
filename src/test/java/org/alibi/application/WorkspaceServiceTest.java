package org.alibi.application;

import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class WorkspaceServiceTest {

    private WorkspaceRepository workspaceRepository;
    private WorkspaceService workspaceService;
    private User mockUser;

    @BeforeEach
    void setUp() {
        workspaceRepository = Mockito.mock(WorkspaceRepository.class);
        workspaceService = new WorkspaceService(workspaceRepository);
        mockUser = new User(1L, "registeredUser", "password"); // Создаем тестового пользователя
    }

    @Test
    @DisplayName("Should add workspace")
    void shouldAddWorkspace() {
        Workspace workspace = new Workspace(1L, "Workspace 1", true);

        workspaceService.addWorkspace(mockUser, workspace);

        Mockito.verify(workspaceRepository).save(workspace);
    }

    @Test
    @DisplayName("Should update workspace")
    void shouldUpdateWorkspace() {
        Workspace workspace = new Workspace(1L, "Workspace 1", true);

        workspaceService.updateWorkspace(mockUser, workspace);

        Mockito.verify(workspaceRepository).update(workspace);
    }

    @Test
    @DisplayName("Should delete workspace")
    void shouldDeleteWorkspace() {
        Long workspaceId = 1L;

        workspaceService.deleteWorkspace(mockUser, workspaceId);

        Mockito.verify(workspaceRepository).delete(workspaceId);
    }

    @Test
    @DisplayName("Should get all workspaces")
    void shouldGetAllWorkspaces() {
        Workspace workspace = new Workspace(1L, "Workspace 1", true);
        when(workspaceRepository.findAll()).thenReturn(List.of(workspace));

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();

        assertThat(workspaces).contains(workspace);
    }
}

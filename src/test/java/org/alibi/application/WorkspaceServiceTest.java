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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class WorkspaceServiceTest {

    private WorkspaceRepository workspaceRepository;
    private AuthorizationService authorizationService;
    private WorkspaceService workspaceService;

    @BeforeEach
    void setUp() {
        workspaceRepository = Mockito.mock(WorkspaceRepository.class);
        authorizationService = Mockito.mock(AuthorizationService.class);
        workspaceService = new WorkspaceService(workspaceRepository, authorizationService);
    }

    @Test
    @DisplayName("Should call repository if admin")
    void addWorkspaceShouldCallRepositoryIfAdmin() {
        User adminUser = new User();
        Workspace workspace = new Workspace();

        when(authorizationService.isAdmin(adminUser)).thenReturn(true);

        workspaceService.addWorkspace(adminUser, workspace);

        verify(workspaceRepository).save(workspace);
    }

    @Test
    @DisplayName("Should throw exception if not admin")
    void addWorkspaceShouldThrowExceptionIfNotAdmin() {
        User user = new User();
        Workspace workspace = new Workspace();

        when(authorizationService.isAdmin(user)).thenReturn(false);

        assertThatThrownBy(() -> workspaceService.addWorkspace(user, workspace))
                .isInstanceOf(SecurityException.class)
                .hasMessage("Only admin can add workspaces.");
    }

    @Test
    @DisplayName("Should call repository if admin")
    void updateWorkspaceShouldCallRepositoryIfAdmin() {
        User adminUser = new User();
        Workspace workspace = new Workspace();

        when(authorizationService.isAdmin(adminUser)).thenReturn(true);

        workspaceService.updateWorkspace(adminUser, workspace);

        verify(workspaceRepository).update(workspace);
    }

    @Test
    @DisplayName("Should throw exception if not admin")
    void updateWorkspaceShouldThrowExceptionIfNotAdmin() {
        User user = new User();
        Workspace workspace = new Workspace();

        when(authorizationService.isAdmin(user)).thenReturn(false);

        assertThatThrownBy(() -> workspaceService.updateWorkspace(user, workspace))
                .isInstanceOf(SecurityException.class)
                .hasMessage("Only admin can update workspaces.");
    }

    @Test
    @DisplayName("Should call repository if admin")
    void deleteWorkspaceShouldCallRepositoryIfAdmin() {
        User adminUser = new User();
        Long workspaceId = 1L;

        when(authorizationService.isAdmin(adminUser)).thenReturn(true);

        workspaceService.deleteWorkspace(adminUser, workspaceId);

        verify(workspaceRepository).delete(workspaceId);
    }

    @Test
    @DisplayName("Should throw exception if not admin")
    void deleteWorkspaceShouldThrowExceptionIfNotAdmin() {
        User user = new User();
        Long workspaceId = 1L;

        when(authorizationService.isAdmin(user)).thenReturn(false);

        assertThatThrownBy(() -> workspaceService.deleteWorkspace(user, workspaceId))
                .isInstanceOf(SecurityException.class)
                .hasMessage("Only admin can delete workspaces.");
    }

    @Test
    @DisplayName("Should return list from repository")
    void getAllWorkspacesShouldReturnListFromRepository() {
        List<Workspace> workspaces = List.of(new Workspace(), new Workspace());

        when(workspaceRepository.findAll()).thenReturn(workspaces);

        List<Workspace> result = workspaceService.getAllWorkspaces();

        assertThat(result).isEqualTo(workspaces);
        verify(workspaceRepository).findAll();
    }
}

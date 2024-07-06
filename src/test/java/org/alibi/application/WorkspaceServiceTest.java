package org.alibi.application;

import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.WorkspaceRepository;
import org.alibi.dto.UserDto;
import org.alibi.dto.WorkspaceDto;
import org.alibi.mapper.WorkspaceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkspaceServiceTest {

    private WorkspaceRepository workspaceRepository;
    private WorkspaceService workspaceService;
    private WorkspaceMapper workspaceMapper = WorkspaceMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        workspaceRepository = mock(WorkspaceRepository.class);
        workspaceService = new WorkspaceService(workspaceRepository);
    }

    @Test
    @DisplayName("Should add workspace successfully")
    void addWorkspace() {
        UserDto userDto = new UserDto(1L, "admin");
        WorkspaceDto workspaceDto = new WorkspaceDto(1L, "Workspace 1", true);

        workspaceService.addWorkspace(userDto, workspaceDto);

        verify(workspaceRepository, times(1)).save(any(Workspace.class));
    }

    @Test
    @DisplayName("Should throw exception when adding workspace with non-registered user")
    void addWorkspace_NotRegisteredUser() {
        UserDto userDto = null;  // Non-registered user
        WorkspaceDto workspaceDto = new WorkspaceDto(1L, "Workspace 1", true);

        assertThrows(SecurityException.class, () -> workspaceService.addWorkspace(userDto, workspaceDto));
    }

    @Test
    @DisplayName("Should update workspace successfully")
    void updateWorkspace() {
        UserDto userDto = new UserDto(1L, "admin");
        WorkspaceDto workspaceDto = new WorkspaceDto(1L, "Workspace 1", true);

        workspaceService.updateWorkspace(userDto, workspaceDto);

        verify(workspaceRepository, times(1)).update(any(Workspace.class));
    }

    @Test
    @DisplayName("Should throw exception when updating workspace with non-registered user")
    void updateWorkspace_NotRegisteredUser() {
        UserDto userDto = null;  // Non-registered user
        WorkspaceDto workspaceDto = new WorkspaceDto(1L, "Workspace 1", true);

        assertThrows(SecurityException.class, () -> workspaceService.updateWorkspace(userDto, workspaceDto));
    }

    @Test
    @DisplayName("Should delete workspace successfully")
    void deleteWorkspace() {
        UserDto userDto = new UserDto(1L, "admin");
        Long workspaceId = 1L;

        workspaceService.deleteWorkspace(userDto, workspaceId);

        verify(workspaceRepository, times(1)).delete(workspaceId);
    }

    @Test
    @DisplayName("Should throw exception when deleting workspace with non-registered user")
    void deleteWorkspace_NotRegisteredUser() {
        UserDto userDto = null;  // Non-registered user
        Long workspaceId = 1L;

        assertThrows(SecurityException.class, () -> workspaceService.deleteWorkspace(userDto, workspaceId));
    }

    @Test
    @DisplayName("Should get all workspaces successfully")
    void getAllWorkspaces() {
        List<Workspace> workspaces = List.of(new Workspace(1L, "Workspace 1", true));
        when(workspaceRepository.findAll()).thenReturn(workspaces);

        List<WorkspaceDto> workspaceDtos = workspaceService.getAllWorkspaces();

        assertThat(workspaceDtos).hasSize(1);
        assertThat(workspaceDtos.get(0).getName()).isEqualTo("Workspace 1");
    }
}


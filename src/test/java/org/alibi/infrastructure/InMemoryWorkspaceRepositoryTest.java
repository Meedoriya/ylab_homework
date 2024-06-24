package org.alibi.infrastructure;

import org.alibi.domain.model.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryWorkspaceRepositoryTest {

    private InMemoryWorkspaceRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkspaceRepository();
    }

    @Test
    @DisplayName("Should add workspace")
    void saveShouldAddWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setName("Workspace 1");

        repository.save(workspace);

        assertThat(repository.getWorkspaces()).contains(workspace);
    }

    @Test
    @DisplayName("Should return workspace when exists")
    void findByIdShouldReturnWorkspaceWhenExists() {
        Workspace workspace = new Workspace();
        workspace.setName("Workspace 1");

        repository.save(workspace);
        Optional<Workspace> found = repository.findById(workspace.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(workspace);
    }

    @Test
    @DisplayName("Should return empty when not exists")
    void findByIdShouldReturnEmptyWhenNotExists() {
        Optional<Workspace> found = repository.findById(1L);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should return all workspaces")
    void findAllShouldReturnAllWorkspaces() {
        Workspace workspace1 = new Workspace();
        workspace1.setName("Workspace 1");

        Workspace workspace2 = new Workspace();
        workspace2.setName("Workspace 2");

        repository.save(workspace1);
        repository.save(workspace2);

        assertThat(repository.findAll()).containsExactlyInAnyOrder(workspace1, workspace2);
    }

    @Test
    @DisplayName("Should return all workspace")
    void updateShouldUpdateWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setName("Workspace 1");

        repository.save(workspace);
        workspace.setName("Updated Workspace 1");
        repository.update(workspace);

        Optional<Workspace> found = repository.findById(workspace.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(workspace.getName());
    }

    @Test
    @DisplayName("Should remove workspace")
    void deleteShouldRemoveWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setName("Workspace 1");

        repository.save(workspace);
        repository.delete(workspace.getId());

        assertThat(repository.findById(workspace.getId())).isEmpty();
    }
}


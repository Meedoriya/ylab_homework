package org.alibi.infrastructure;

import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.WorkspaceRepository;
import org.alibi.in.DatabaseInitializer;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class WorkspaceRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("postgres")
            .withUsername("alibi")
            .withPassword("alibi");

    private Connection connection;
    private WorkspaceRepository workspaceRepository;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
        DatabaseInitializer.initialize(connection);
        workspaceRepository = new WorkspaceRepositoryImpl(connection);
        workspaceRepository.deleteAll();
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    @DisplayName("Should save and find workspace by ID")
    void testSaveAndFindById() {
        Workspace workspace = new Workspace(null, "Workspace 1", true);
        workspaceRepository.save(workspace);

        Optional<Workspace> retrievedWorkspace = workspaceRepository.findById(workspace.getId());
        assertThat(retrievedWorkspace).isPresent();
        assertThat(retrievedWorkspace.get().getName()).isEqualTo("Workspace 1");
    }

    @Test
    @DisplayName("Should update workspace")
    void testUpdate() {
        Workspace workspace = new Workspace(null, "Workspace 1", true);
        workspaceRepository.save(workspace);

        workspace.setName("Updated Workspace");
        workspaceRepository.update(workspace);

        Optional<Workspace> retrievedWorkspace = workspaceRepository.findById(workspace.getId());
        assertThat(retrievedWorkspace).isPresent();
        assertThat(retrievedWorkspace.get().getName()).isEqualTo("Updated Workspace");
    }

    @Test
    @DisplayName("Should delete workspace")
    void testDelete() {
        Workspace workspace = new Workspace(null, "Workspace 1", true);
        workspaceRepository.save(workspace);

        workspaceRepository.delete(workspace.getId());

        Optional<Workspace> retrievedWorkspace = workspaceRepository.findById(workspace.getId());
        assertThat(retrievedWorkspace).isNotPresent();
    }

    @Test
    @DisplayName("Should find all workspaces")
    void testFindAll() {
        Workspace workspace1 = new Workspace(null, "Workspace 1", true);
        Workspace workspace2 = new Workspace(null, "Workspace 2", true);

        workspaceRepository.save(workspace1);
        workspaceRepository.save(workspace2);

        List<Workspace> workspaces = workspaceRepository.findAll();
        assertThat(workspaces).hasSize(2);
        assertThat(workspaces).extracting("name").contains("Workspace 1", "Workspace 2");
    }
}

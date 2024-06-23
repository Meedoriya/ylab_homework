package org.alibi.domain.repository;

import org.alibi.domain.model.Workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository {
    void save(Workspace workspace);
    Optional<Workspace> findById(Long id);
    List<Workspace> findAll();
    void update(Workspace workspace);
    void delete(Long id);
}

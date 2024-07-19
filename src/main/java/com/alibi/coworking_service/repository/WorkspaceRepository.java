package com.alibi.coworking_service.repository;

import com.alibi.coworking_service.domain.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}

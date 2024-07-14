package org.alibi.service.impl;

import org.alibi.domain.dto.WorkspaceDto;
import org.alibi.domain.model.Workspace;
import org.alibi.mapper.WorkspaceMapper;
import org.alibi.repository.WorkspaceRepository;
import org.alibi.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Реализация сервиса для управления рабочими местами.
 */
@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;

    @Override
    public void addWorkspace(WorkspaceDto workspaceDto) {
        Workspace workspace = workspaceMapper.toEntity(workspaceDto);
        workspaceRepository.save(workspace);
    }

    @Override
    public void updateWorkspace(WorkspaceDto workspaceDto) {
        Workspace workspace = workspaceMapper.toEntity(workspaceDto);
        workspaceRepository.update(workspace);
    }

    @Override
    public void deleteWorkspace(Long id) {
        workspaceRepository.delete(id);
    }

    @Override
    public List<WorkspaceDto> getAllWorkspaces() {
        return workspaceRepository.findAll().stream()
                .map(workspaceMapper::toDto)
                .collect(Collectors.toList());
    }
}

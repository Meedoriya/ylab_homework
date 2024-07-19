package com.alibi.coworking_service.service.impl;

import com.alibi.coworking_service.domain.dto.WorkspaceDto;
import com.alibi.coworking_service.domain.model.ConferenceRoom;
import com.alibi.coworking_service.domain.model.Workspace;
import com.alibi.coworking_service.exceptions.NotFoundException;
import com.alibi.coworking_service.mapper.WorkspaceMapper;
import com.alibi.coworking_service.repository.WorkspaceRepository;
import com.alibi.coworking_service.service.WorkspaceService;
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
    public WorkspaceDto updateWorkspace(Long workspaceId, WorkspaceDto workspaceDto) {
        Workspace workspace = getWorkspaceOrThrowException(workspaceId);

        workspace.setName(workspaceDto.getName());
        workspace.setAvailable(workspaceDto.getAvailable());

        Workspace updatedWorkspace = workspaceRepository.save(workspace);

        return workspaceMapper.toDto(workspace);
    }

    @Override
    public void deleteWorkspace(Long id) {
        workspaceRepository.deleteById(id);
    }

    @Override
    public List<WorkspaceDto> getAllWorkspaces() {
        return workspaceRepository.findAll().stream()
                .map(workspaceMapper::toDto)
                .collect(Collectors.toList());
    }

    private Workspace getWorkspaceOrThrowException(Long workspaceId) {
        return workspaceRepository
                .findById(workspaceId)
                .orElseThrow(() -> new NotFoundException(String.format("Course with id \"%s\" doesn't exist.", workspaceId)));
    }
}
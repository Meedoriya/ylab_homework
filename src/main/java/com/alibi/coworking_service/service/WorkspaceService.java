package com.alibi.coworking_service.service;

import com.alibi.coworking_service.domain.dto.WorkspaceDto;

import java.util.List;

/**
 * Интерфейс сервиса для управления рабочими местами.
 */
public interface WorkspaceService {
    /**
     * Добавляет новое рабочее место.
     * @param workspaceDto рабочее место для добавления
     */
    void addWorkspace(WorkspaceDto workspaceDto);

    /**
     * Обновляет существующее рабочее место.
     * @param workspaceDto рабочее место для обновления
     */
    WorkspaceDto updateWorkspace(Long workspaceId, WorkspaceDto workspaceDto);

    /**
     * Удаляет рабочее место.
     * @param id ID рабочего места
     */
    void deleteWorkspace(Long id);

    /**
     * Возвращает все рабочие места.
     * @return список всех рабочих мест
     */
    List<WorkspaceDto> getAllWorkspaces();
}

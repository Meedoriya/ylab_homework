package org.alibi.service;

import org.alibi.domain.dto.WorkspaceDto;

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
    void updateWorkspace(WorkspaceDto workspaceDto);

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

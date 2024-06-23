package org.alibi.application;


import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.WorkspaceRepository;

import java.util.List;

/**
 * Сервис для управления рабочими местами.
 */
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final AuthorizationService authorizationService;

    /**
     * Добавляет новое рабочее место.
     *
     * @param user     Пользователь, выполняющий операцию.
     * @param workspace Рабочее место для добавления.
     * @throws SecurityException если пользователь не является администратором.
     */
    public void addWorkspace(User user, Workspace workspace) {
        if (authorizationService.isAdmin(user)) {
            workspaceRepository.save(workspace);
        } else {
            throw new SecurityException("Only admin can add workspaces.");
        }
    }

    /**
     * Обновляет существующее рабочее место.
     *
     * @param user     Пользователь, выполняющий операцию.
     * @param workspace Рабочее место для обновления.
     * @throws SecurityException если пользователь не является администратором.
     */
    public void updateWorkspace(User user, Workspace workspace) {
        if (authorizationService.isAdmin(user)) {
            workspaceRepository.update(workspace);
        } else {
            throw new SecurityException("Only admin can update workspaces.");
        }
    }

    /**
     * Удаляет рабочее место по его ID.
     *
     * @param user     Пользователь, выполняющий операцию.
     * @param id       ID рабочего места для удаления.
     * @throws SecurityException если пользователь не является администратором.
     */
    public void deleteWorkspace(User user, Long id) {
        if (authorizationService.isAdmin(user)) {
            workspaceRepository.delete(id);
        } else {
            throw new SecurityException("Only admin can delete workspaces.");
        }
    }

    /**
     * Возвращает список всех рабочих мест.
     *
     * @return Список всех рабочих мест.
     */
    public List<Workspace> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }
}

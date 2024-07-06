package org.alibi.application;

import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.User;
import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.WorkspaceRepository;
import org.alibi.dto.UserDto;
import org.alibi.dto.WorkspaceDto;
import org.alibi.mapper.WorkspaceMapper;

import java.util.List;

/**
 * Сервис для управления рабочими местами.
 */
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper = WorkspaceMapper.INSTANCE;

    /**
     * Добавляет новое рабочее место.
     *
     * @param userDto     Пользователь, выполняющий операцию.
     * @param workspaceDto Рабочее место для добавления.
     * @throws SecurityException если пользователь не является зарегистрированным.
     */
    public void addWorkspace(UserDto userDto, WorkspaceDto workspaceDto) {
        if (isRegisteredUser(userDto)) {
            Workspace workspace = workspaceMapper.toEntity(workspaceDto);
            workspaceRepository.save(workspace);
        } else {
            throw new SecurityException("Only registered users can add workspaces.");
        }
    }

    /**
     * Обновляет существующее рабочее место.
     *
     * @param user     Пользователь, выполняющий операцию.
     * @param workspaceDto Рабочее место для обновления.
     * @throws SecurityException если пользователь не является зарегистрированным.
     */
    public void updateWorkspace(UserDto userDto, WorkspaceDto workspaceDto) {
        if (isRegisteredUser(userDto)) {
            Workspace workspace = workspaceMapper.toEntity(workspaceDto);
            workspaceRepository.update(workspace);
        } else {
            throw new SecurityException("Only registered users can update workspaces.");
        }
    }
    /**
     * Удаляет рабочее место по его ID.
     *
     * @param user     Пользователь, выполняющий операцию.
     * @param id       ID рабочего места для удаления.
     * @throws SecurityException если пользователь не является зарегистрированным.
     */
    public void deleteWorkspace(UserDto userDto, Long id) {
        if (isRegisteredUser(userDto)) {
            workspaceRepository.delete(id);
        } else {
            throw new SecurityException("Only registered users can delete workspaces.");
        }
    }

    /**
     * Возвращает список всех рабочих мест.
     *
     * @return Список всех рабочих мест.
     */
    public List<WorkspaceDto> getAllWorkspaces() {
        return workspaceRepository.findAll().stream()
                .map(workspaceMapper::toDto)
                .toList();
    }

    private boolean isRegisteredUser(UserDto userDto) {
        // Здесь можно добавить логику проверки, зарегистрирован ли пользователь
        return userDto != null;
    }
}

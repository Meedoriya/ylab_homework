package org.alibi.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.alibi.domain.model.Workspace;
import org.alibi.domain.repository.WorkspaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для хранения рабочих мест в памяти.
 */
@Getter
@Setter
public class InMemoryWorkspaceRepository implements WorkspaceRepository {
    private final List<Workspace> workspaces = new ArrayList<>();
    private Long counter = 1L;

    /**
     * Сохраняет новое рабочее место.
     *
     * @param workspace Рабочее место для сохранения.
     */
    @Override
    public void save(Workspace workspace) {
        workspace.setId(counter++);
        workspaces.add(workspace);
    }

    /**
     * Находит рабочее место по его ID.
     *
     * @param id ID рабочего места.
     * @return Опциональное рабочее место.
     */
    @Override
    public Optional<Workspace> findById(Long id) {
        return workspaces.stream()
                .filter(workspace -> workspace.getId().equals(id)).findFirst();
    }

    /**
     * Возвращает все рабочие места.
     *
     * @return Список всех рабочих мест.
     */
    @Override
    public List<Workspace> findAll() {
        return new ArrayList<>(workspaces);
    }

    /**
     * Обновляет существующее рабочее место.
     *
     * @param workspace Рабочее место для обновления.
     */
    @Override
    public void update(Workspace workspace) {
        delete(workspace.getId());
        workspaces.add(workspace);
    }

    /**
     * Удаляет рабочее место по его ID.
     *
     * @param id ID рабочего места для удаления.
     */
    @Override
    public void delete(Long id) {
        workspaces.removeIf(workspace -> workspace.getId().equals(id));
    }
}


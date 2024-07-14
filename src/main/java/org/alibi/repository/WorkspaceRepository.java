package org.alibi.repository;

import org.alibi.domain.model.Workspace;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для управления рабочими местами.
 */
public interface WorkspaceRepository {
    /**
     * Сохраняет новое рабочее место.
     * @param workspace рабочее место для сохранения
     */
    void save(Workspace workspace);

    /**
     * Ищет рабочее место по его ID.
     * @param id ID рабочего места
     * @return Optional с найденным рабочим местом, если оно существует
     */
    Optional<Workspace> findById(Long id);

    /**
     * Возвращает все рабочие места.
     * @return список всех рабочих мест
     */
    List<Workspace> findAll();

    /**
     * Обновляет существующее рабочее место.
     * @param workspace рабочее место для обновления
     */
    void update(Workspace workspace);

    /**
     * Удаляет рабочее место по его ID.
     * @param id ID рабочего места
     */
    void delete(Long id);

    /**
     * Удаляет все рабочие места.
     */
    void deleteAll();
}

package org.alibi.repository;

import org.alibi.domain.model.ConferenceRoom;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для управления конференц-залами.
 */
public interface ConferenceRoomRepository {
    /**
     * Сохраняет новый конференц-зал.
     * @param conferenceRoom конференц-зал для сохранения
     */
    void save(ConferenceRoom conferenceRoom);

    /**
     * Ищет конференц-зал по его ID.
     * @param id ID конференц-зала
     * @return Optional с найденным конференц-залом, если он существует
     */
    Optional<ConferenceRoom> findById(Long id);

    /**
     * Возвращает все конференц-залы.
     * @return список всех конференц-залов
     */
    List<ConferenceRoom> findAll();

    /**
     * Обновляет существующий конференц-зал.
     * @param conferenceRoom конференц-зал для обновления
     */
    void update(ConferenceRoom conferenceRoom);

    /**
     * Удаляет конференц-зал по его ID.
     * @param id ID конференц-зала
     */
    void delete(Long id);

    /**
     * Удаляет все конференц-залы.
     */
    void deleteAll();
}

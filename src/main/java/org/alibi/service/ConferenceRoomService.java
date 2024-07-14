package org.alibi.service;

import org.alibi.domain.dto.ConferenceRoomDto;
import org.alibi.domain.model.ConferenceRoom;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс сервиса для управления конференц-залами.
 */
public interface ConferenceRoomService {
    /**
     * Добавляет новый конференц-зал.
     * @param conferenceRoomDto конференц-зал для добавления
     */
    void addConferenceRoom(ConferenceRoomDto conferenceRoomDto);

    /**
     * Обновляет существующий конференц-зал.
     * @param conferenceRoomDto конференц-зал для обновления
     */
    void updateConferenceRoom(ConferenceRoomDto conferenceRoomDto);

    /**
     * Удаляет конференц-зал.
     * @param id ID конференц-зала
     */
    void deleteConferenceRoom(Long id);

    /**
     * Возвращает все конференц-залы.
     * @return список всех конференц-залов
     */
    List<ConferenceRoomDto> getAllConferenceRooms();

    /**
     * Возвращает конференц-зал по его ID.
     * @param id ID конференц-зала
     * @return Optional с найденным конференц-залом, если он существует
     */
    Optional<ConferenceRoomDto> getConferenceRoomById(Long id);
}

package org.alibi.application;

import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.User;
import org.alibi.domain.repository.ConferenceRoomRepository;

import java.util.List;

/**
 * Сервис для управления конференц-залами.
 */
@RequiredArgsConstructor
public class ConferenceRoomService {
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final AuthorizationService authorizationService;

    /**
     * Добавляет новый конференц-зал.
     *
     * @param user          Пользователь, выполняющий операцию.
     * @param conferenceRoom Конференц-зал для добавления.
     * @throws SecurityException если пользователь не является администратором.
     */
    public void addConferenceRoom(User user, ConferenceRoom conferenceRoom) {
        if (authorizationService.isAdmin(user)) {
            conferenceRoomRepository.save(conferenceRoom);
        } else {
            throw new SecurityException("Only admin can add conference rooms.");
        }
    }

    /**
     * Обновляет существующий конференц-зал.
     *
     * @param user          Пользователь, выполняющий операцию.
     * @param conferenceRoom Конференц-зал для обновления.
     * @throws SecurityException если пользователь не является администратором.
     */
    public void updateConferenceRoom(User user, ConferenceRoom conferenceRoom) {
        if (authorizationService.isAdmin(user)) {
            conferenceRoomRepository.update(conferenceRoom);
        } else {
            throw new SecurityException("Only admin can update conference rooms.");
        }
    }

    /**
     * Удаляет конференц-зал по его ID.
     *
     * @param user Пользователь, выполняющий операцию.
     * @param id   ID конференц-зала для удаления.
     * @throws SecurityException если пользователь не является администратором.
     */
    public void deleteConferenceRoom(User user, Long id) {
        if (authorizationService.isAdmin(user)) {
            conferenceRoomRepository.delete(id);
        } else {
            throw new SecurityException("Only admin can delete conference rooms.");
        }
    }

    /**
     * Возвращает список всех конференц-залов.
     *
     * @return Список всех конференц-залов.
     */
    public List<ConferenceRoom> getAllConferenceRooms() {
        return conferenceRoomRepository.findAll();
    }
}

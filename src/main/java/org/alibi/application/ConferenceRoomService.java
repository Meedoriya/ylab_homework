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

    /**
     * Добавляет новый конференц-зал.
     *
     * @param user          Пользователь, выполняющий операцию.
     * @param conferenceRoom Конференц-зал для добавления.
     */
    public void addConferenceRoom(User user, ConferenceRoom conferenceRoom) {
        if (isRegisteredUser(user)) {
            conferenceRoomRepository.save(conferenceRoom);
        } else {
            throw new SecurityException("Only registered users can add conference rooms.");
        }
    }

    /**
     * Обновляет существующий конференц-зал.
     *
     * @param user          Пользователь, выполняющий операцию.
     * @param conferenceRoom Конференц-зал для обновления.
     */
    public void updateConferenceRoom(User user, ConferenceRoom conferenceRoom) {
        if (isRegisteredUser(user)) {
            conferenceRoomRepository.update(conferenceRoom);
        } else {
            throw new SecurityException("Only registered users can update conference rooms.");
        }
    }

    /**
     * Удаляет конференц-зал по его ID.
     *
     * @param user Пользователь, выполняющий операцию.
     * @param id   ID конференц-зала для удаления.
     */
    public void deleteConferenceRoom(User user, Long id) {
        if (isRegisteredUser(user)) {
            conferenceRoomRepository.delete(id);
        } else {
            throw new SecurityException("Only registered users can delete conference rooms.");
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

    private boolean isRegisteredUser(User user) {
        // Здесь можно добавить логику проверки, зарегистрирован ли пользователь
        return user != null;
    }
}

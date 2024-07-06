package org.alibi.application;

import lombok.RequiredArgsConstructor;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.model.User;
import org.alibi.domain.repository.ConferenceRoomRepository;
import org.alibi.dto.ConferenceRoomDto;
import org.alibi.dto.UserDto;
import org.alibi.mapper.ConferenceRoomMapper;

import java.util.List;

/**
 * Сервис для управления конференц-залами.
 */
@RequiredArgsConstructor
public class ConferenceRoomService {
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final ConferenceRoomMapper conferenceRoomMapper = ConferenceRoomMapper.INSTANCE;

    /**
     * Добавляет новый конференц-зал.
     *
     * @param userDto           Пользователь, выполняющий операцию.
     * @param conferenceRoomDto Конференц-зал для добавления.
     */
    public void addConferenceRoom(UserDto userDto, ConferenceRoomDto conferenceRoomDto) {
        if (isRegisteredUser(userDto)) {
            ConferenceRoom conferenceRoom = conferenceRoomMapper.toEntity(conferenceRoomDto);
            conferenceRoomRepository.save(conferenceRoom);
        } else {
            throw new SecurityException("Only registered users can add conference rooms.");
        }
    }


    /**
     * Обновляет существующий конференц-зал.
     *
     * @param userDto           Пользователь, выполняющий операцию.
     * @param conferenceRoomDto Конференц-зал для обновления.
     */
    public void updateConferenceRoom(UserDto userDto, ConferenceRoomDto conferenceRoomDto) {
        if (isRegisteredUser(userDto)) {
            ConferenceRoom conferenceRoom = conferenceRoomMapper.toEntity(conferenceRoomDto);
            conferenceRoomRepository.update(conferenceRoom);
        } else {
            throw new SecurityException("Only registered users can update conference rooms.");
        }
    }

    /**
     * Удаляет конференц-зал по его ID.
     *
     * @param userDto Пользователь, выполняющий операцию.
     * @param id      ID конференц-зала для удаления.
     */
    public void deleteConferenceRoom(UserDto userDto, Long id) {
        if (isRegisteredUser(userDto)) {
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
    public List<ConferenceRoomDto> getAllConferenceRooms() {
        return conferenceRoomRepository.findAll().stream()
                .map(conferenceRoomMapper::toDto)
                .toList();
    }

    private boolean isRegisteredUser(UserDto userDto) {
        // Здесь можно добавить логику проверки, зарегистрирован ли пользователь
        return userDto != null;
    }
}

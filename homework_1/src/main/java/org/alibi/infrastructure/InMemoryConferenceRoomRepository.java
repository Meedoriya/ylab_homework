package org.alibi.infrastructure;

import lombok.Getter;
import lombok.Setter;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.domain.repository.ConferenceRoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для хранения конференц-залов в памяти.
 */
@Getter
@Setter
public class InMemoryConferenceRoomRepository implements ConferenceRoomRepository {
    private final List<ConferenceRoom> conferenceRooms = new ArrayList<>();
    private Long counter = 1L;

    /**
     * Сохраняет новый конференц-зал.
     *
     * @param conferenceRoom Конференц-зал для сохранения.
     */
    @Override
    public void save(ConferenceRoom conferenceRoom) {
        conferenceRoom.setId(counter++);
        conferenceRooms.add(conferenceRoom);
    }

    /**
     * Находит конференц-зал по его ID.
     *
     * @param id ID конференц-зала.
     * @return Опциональный конференц-зал.
     */
    @Override
    public Optional<ConferenceRoom> findById(Long id) {
        return conferenceRooms.stream()
                .filter(conferenceRoom -> conferenceRoom.getId().equals(id)).findFirst();
    }

    /**
     * Возвращает все конференц-залы.
     *
     * @return Список всех конференц-залов.
     */
    @Override
    public List<ConferenceRoom> findAll() {
        return new ArrayList<>(conferenceRooms);
    }

    /**
     * Обновляет существующий конференц-зал.
     *
     * @param conferenceRoom Конференц-зал для обновления.
     */
    @Override
    public void update(ConferenceRoom conferenceRoom) {
        delete(conferenceRoom.getId());
        conferenceRooms.add(conferenceRoom);
    }

    /**
     * Удаляет конференц-зал по его ID.
     *
     * @param id ID конференц-зала для удаления.
     */
    @Override
    public void delete(Long id) {
        conferenceRooms.removeIf(conferenceRoom -> conferenceRoom.getId().equals(id));
    }
}

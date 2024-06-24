package org.alibi.infrastructure;

import org.alibi.domain.model.ConferenceRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryConferenceRoomRepositoryTest {

    private InMemoryConferenceRoomRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryConferenceRoomRepository();
    }

    @Test
    @DisplayName("Should add conference room")
    void saveShouldAddConferenceRoom() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("Conference Room 1");

        repository.save(conferenceRoom);

        assertThat(repository.getConferenceRooms()).contains(conferenceRoom);
    }

    @Test
    @DisplayName("Should return conference room when exists")
    void findByIdShouldReturnConferenceRoomWhenExists() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("Conference Room 1");

        repository.save(conferenceRoom);
        Optional<ConferenceRoom> found = repository.findById(conferenceRoom.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(conferenceRoom);
    }

    @Test
    @DisplayName("Should return empty when not exists")
    void findByIdShouldReturnEmptyWhenNotExists() {
        Optional<ConferenceRoom> found = repository.findById(1L);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should return all conference rooms")
    void findAllShouldReturnAllConferenceRooms() {
        ConferenceRoom conferenceRoom1 = new ConferenceRoom();
        conferenceRoom1.setName("Conference Room 1");

        ConferenceRoom conferenceRoom2 = new ConferenceRoom();
        conferenceRoom2.setName("Conference Room 2");

        repository.save(conferenceRoom1);
        repository.save(conferenceRoom2);

        assertThat(repository.findAll()).containsExactlyInAnyOrder(conferenceRoom1, conferenceRoom2);
    }

    @Test
    @DisplayName("Should update conference room")
    void updateShouldUpdateConferenceRoom() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("Conference Room 1");

        repository.save(conferenceRoom);
        conferenceRoom.setName("Updated Conference Room 1");
        repository.update(conferenceRoom);

        Optional<ConferenceRoom> found = repository.findById(conferenceRoom.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(conferenceRoom.getName());
    }

    @Test
    @DisplayName("Should remove conference room")
    void deleteShouldRemoveConferenceRoom() {
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setName("Conference Room 1");

        repository.save(conferenceRoom);
        repository.delete(conferenceRoom.getId());

        assertThat(repository.findById(conferenceRoom.getId())).isEmpty();
    }
}


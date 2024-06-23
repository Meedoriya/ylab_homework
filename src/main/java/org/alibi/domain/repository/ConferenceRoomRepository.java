package org.alibi.domain.repository;

import org.alibi.domain.model.ConferenceRoom;

import java.util.List;
import java.util.Optional;

public interface ConferenceRoomRepository {
    void save(ConferenceRoom conferenceRoom);
    Optional<ConferenceRoom> findById(Long id);
    List<ConferenceRoom> findAll();
    void update(ConferenceRoom conferenceRoom);
    void delete(Long id);
}

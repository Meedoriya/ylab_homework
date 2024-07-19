package com.alibi.coworking_service.repository;

import com.alibi.coworking_service.domain.model.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Long> {

}

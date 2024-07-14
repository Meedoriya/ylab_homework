package org.alibi.service.impl;

import org.alibi.domain.dto.ConferenceRoomDto;
import org.alibi.domain.model.ConferenceRoom;
import org.alibi.mapper.ConferenceRoomMapper;
import org.alibi.repository.ConferenceRoomRepository;
import org.alibi.service.ConferenceRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Реализация сервиса для управления конференц-залами.
 */
@Service
@RequiredArgsConstructor
public class ConferenceRoomServiceImpl implements ConferenceRoomService {

    private final ConferenceRoomRepository conferenceRoomRepository;
    private final ConferenceRoomMapper conferenceRoomMapper;

    @Override
    public void addConferenceRoom(ConferenceRoomDto conferenceRoomDto) {
        ConferenceRoom conferenceRoom = conferenceRoomMapper.toEntity(conferenceRoomDto);
        conferenceRoomRepository.save(conferenceRoom);
    }

    @Override
    public void updateConferenceRoom(ConferenceRoomDto conferenceRoomDto) {
        ConferenceRoom conferenceRoom = conferenceRoomMapper.toEntity(conferenceRoomDto);
        conferenceRoomRepository.update(conferenceRoom);
    }

    @Override
    public void deleteConferenceRoom(Long id) {
        conferenceRoomRepository.delete(id);
    }

    @Override
    public List<ConferenceRoomDto> getAllConferenceRooms() {
        return conferenceRoomRepository.findAll().stream()
                .map(conferenceRoomMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ConferenceRoomDto> getConferenceRoomById(Long id) {
        return conferenceRoomRepository.findById(id).map(conferenceRoomMapper::toDto);
    }
}

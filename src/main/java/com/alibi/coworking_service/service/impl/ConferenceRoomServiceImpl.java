package com.alibi.coworking_service.service.impl;

import com.alibi.coworking_service.domain.dto.ConferenceRoomDto;
import com.alibi.coworking_service.domain.model.ConferenceRoom;
import com.alibi.coworking_service.exceptions.NotFoundException;
import com.alibi.coworking_service.mapper.ConferenceRoomMapper;
import com.alibi.coworking_service.repository.ConferenceRoomRepository;
import com.alibi.coworking_service.service.ConferenceRoomService;
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
    public ConferenceRoomDto updateConferenceRoom(Long conferenceRoomId, ConferenceRoomDto conferenceRoomDto) {
        ConferenceRoom conferenceRoom = getConferenceRoomOrThrowException(conferenceRoomId);

        conferenceRoom.setName(conferenceRoomDto.getName());
        conferenceRoom.setAvailable(conferenceRoomDto.isAvailable());

        ConferenceRoom updatedConferenceRoom = conferenceRoomRepository.save(conferenceRoom);

        return conferenceRoomMapper.toDto(updatedConferenceRoom);
    }

    @Override
    public void deleteConferenceRoom(Long id) {
        conferenceRoomRepository.deleteById(id);
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

    public ConferenceRoom getConferenceRoomOrThrowException(Long conferenceRoomId) {
        return conferenceRoomRepository
                .findById(conferenceRoomId)
                .orElseThrow(() -> new NotFoundException(String.format("Course with id \"%s\" doesn't exist.", conferenceRoomId)));
    }
}

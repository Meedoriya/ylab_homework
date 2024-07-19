package com.alibi.coworking_service.mapper;

import com.alibi.coworking_service.domain.dto.ConferenceRoomDto;
import com.alibi.coworking_service.domain.model.ConferenceRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConferenceRoomMapper {

    ConferenceRoomDto toDto(ConferenceRoom conferenceRoom);

    ConferenceRoom toEntity(ConferenceRoomDto conferenceRoomDto);
}

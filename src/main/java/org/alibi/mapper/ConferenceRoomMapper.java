package org.alibi.mapper;

import org.alibi.domain.model.ConferenceRoom;
import org.alibi.dto.ConferenceRoomDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper для преобразования между ConferenceRoom и ConferenceRoomDto.
 */
@Mapper
public interface ConferenceRoomMapper {

    ConferenceRoomMapper INSTANCE = Mappers.getMapper(ConferenceRoomMapper.class);

    ConferenceRoomDto toDto(ConferenceRoom conferenceRoom);

    ConferenceRoom toEntity(ConferenceRoomDto conferenceRoomDto);
}

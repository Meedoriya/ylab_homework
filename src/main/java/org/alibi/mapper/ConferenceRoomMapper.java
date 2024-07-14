package org.alibi.mapper;

import org.alibi.domain.dto.ConferenceRoomDto;
import org.alibi.domain.model.ConferenceRoom;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper для преобразования между ConferenceRoom и ConferenceRoomDto.
 */
@Mapper(componentModel = "spring")
@Component
public interface ConferenceRoomMapper {

    ConferenceRoomDto toDto(ConferenceRoom conferenceRoom);

    ConferenceRoom toEntity(ConferenceRoomDto conferenceRoomDto);
}

package org.alibi.mapper;

import org.alibi.domain.model.Booking;
import org.alibi.dto.BookingDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper для преобразования между Booking и BookingDto.
 */
@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingDto toDto(Booking booking);

    Booking toEntity(BookingDto bookingDto);
}

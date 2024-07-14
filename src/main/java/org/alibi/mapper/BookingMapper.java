package org.alibi.mapper;

import org.alibi.domain.dto.BookingDto;
import org.alibi.domain.model.Booking;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper для преобразования между Booking и BookingDto.
 */
@Mapper(componentModel = "spring")
@Component
public interface BookingMapper {
    BookingDto toDto(Booking booking);

    Booking toEntity(BookingDto bookingDto);
}

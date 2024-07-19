package com.alibi.coworking_service.mapper;

import com.alibi.coworking_service.domain.dto.BookingDto;
import com.alibi.coworking_service.domain.model.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDto toDto(Booking booking);

    Booking toEntity(BookingDto bookingDto);
}

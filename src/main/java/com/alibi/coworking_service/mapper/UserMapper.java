package com.alibi.coworking_service.mapper;

import com.alibi.coworking_service.domain.dto.UserDto;
import com.alibi.coworking_service.domain.dto.UserRegistrationDto;
import com.alibi.coworking_service.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);
}

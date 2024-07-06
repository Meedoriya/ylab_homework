package org.alibi.mapper;

import org.alibi.domain.model.User;
import org.alibi.dto.UserDto;
import org.alibi.dto.UserRegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper для преобразования между User и UserDto.
 */
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);
}

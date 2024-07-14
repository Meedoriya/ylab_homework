package org.alibi.mapper;

import org.alibi.domain.dto.UserDto;
import org.alibi.domain.dto.UserRegistrationDto;
import org.alibi.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * Mapper для преобразования между User и UserDto.
 */
@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);
}

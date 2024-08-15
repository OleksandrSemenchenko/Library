package com.nerdysoft.library.mapper;

import com.nerdysoft.library.repository.entity.User;
import com.nerdysoft.library.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Mapper interface for the User entity. The interface definition is used by MapStruct processor to
 * generate UserMapperImpl class under target directory when compiled. Refer https://mapstruct.org
 * for more details.
 *
 * @author Oleksandr Semenchenko
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

  User toEntity(UserDto userDto);

  UserDto toDto(User user);
}

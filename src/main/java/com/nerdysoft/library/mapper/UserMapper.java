package com.nerdysoft.library.mapper;

import com.nerdysoft.library.repository.entity.User;
import com.nerdysoft.library.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

  UserDto toDto(User user);
}

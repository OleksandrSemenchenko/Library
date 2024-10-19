package com.nerdysoft.library.service;

import com.nerdysoft.library.service.dto.UserDto;
import java.util.UUID;

public interface UserService {

  UserDto updateUser(UserDto userDto);

  UserDto createUser(UserDto userDto);

  void deleteUser(UUID userId);

  void borrowBookByUser(UUID userId, UUID bookId);

  UserDto getUserById(UUID userId);
}

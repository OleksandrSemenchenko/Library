package com.nerdysoft.library.service;

import com.nerdysoft.library.service.dto.UserDto;

import java.util.UUID;

public interface UserService {

  UserDto getUser(UUID userId);
}

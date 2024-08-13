package com.nerdysoft.library.service.impl;

import com.nerdysoft.library.exceptionhandler.ExceptionMessages;
import com.nerdysoft.library.exceptionhandler.exceptions.UserNotFoundException;
import com.nerdysoft.library.mapper.UserMapper;
import com.nerdysoft.library.repository.UserRepository;
import com.nerdysoft.library.repository.entity.User;
import com.nerdysoft.library.service.UserService;
import com.nerdysoft.library.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto getUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND.formatted(userId)));
    return userMapper.toDto(user);
  }
}

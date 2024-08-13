package com.nerdysoft.library.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.nerdysoft.library.TestDataGenerator;
import com.nerdysoft.library.exceptionhandler.exceptions.UserNotFoundException;
import com.nerdysoft.library.mapper.UserMapper;
import com.nerdysoft.library.repository.UserRepository;
import com.nerdysoft.library.repository.entity.User;
import com.nerdysoft.library.service.dto.UserDto;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  private static final UUID USER_ID = UUID.fromString("f0d9bdfc-38e7-4a34-b07f-8216574efbb5");
  private static final UUID NOT_EXISTING_USER_ID = UUID.randomUUID();

  @InjectMocks private UserServiceImpl userService;

  @Mock private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    ReflectionTestUtils.setField(userService, "userMapper", userMapper);
  }

  @Test
  void getUser_shouldReturnUser_whenUserIsInDb() {
    User user = TestDataGenerator.generateUser();

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    UserDto retrivedUserDto = userService.getUser(USER_ID);

    assertEquals(user.getId(), retrivedUserDto.getId());
    assertEquals(user.getName(), retrivedUserDto.getName());
    assertEquals(user.getMembershipDate(), retrivedUserDto.getMembershipDate());
  }

  @Test
  void getUser_shouldReturnUser_whenNoUserInDb() {
    Mockito.when(userRepository.findById(NOT_EXISTING_USER_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUser(NOT_EXISTING_USER_ID));
  }
}

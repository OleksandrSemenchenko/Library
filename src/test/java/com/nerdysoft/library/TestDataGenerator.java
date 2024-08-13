package com.nerdysoft.library;

import com.nerdysoft.library.repository.entity.User;
import com.nerdysoft.library.service.dto.UserDto;

import java.time.LocalDate;
import java.util.UUID;

public class TestDataGenerator {

  private static final UUID USER_ID = UUID.fromString("f0d9bdfc-38e7-4a34-b07f-8216574efbb5");
  private static final String USER_NAME = "John Doe";
  private static final LocalDate MEMBERSHIP_DATE = LocalDate.of(2024, 8, 13);

  public static UserDto generateUserDto() {
    return UserDto.builder()
        .id(USER_ID)
        .name(USER_NAME)
        .membershipDate(MEMBERSHIP_DATE)
        .build();
  }

  public static User generateUser() {
    return User.builder()
        .id(USER_ID)
        .name(USER_NAME)
        .membershipDate(MEMBERSHIP_DATE)
        .build();
  }
}

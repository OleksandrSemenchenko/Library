package com.nerdysoft.library.controller;

import com.nerdysoft.library.service.UserService;
import com.nerdysoft.library.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

  private static final String V1 = "/v1";
  private static final String USER_ID_PATH = "/users/{userId}";

  private final UserService userService;

  @GetMapping(value = V1 + USER_ID_PATH)
  public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
    UserDto user = userService.getUser(userId);
    return ResponseEntity.ok(user);
  }
}

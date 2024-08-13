package com.nerdysoft.library.controller;

import com.nerdysoft.library.service.UserService;
import com.nerdysoft.library.service.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST controller to support end points to manage users.
 *
 * @author Oleksandr Semenchenko
 */
@RestController
@RequiredArgsConstructor
public class UserController {

  private static final String V1 = "/v1";
  private static final String USER_ID_PATH = "/users/{userId}";

  private static final String USER_NOT_FOUND_ERROR =
      """
      {
          "timestamp": "2024-08-13T15:26:57.322006719",
          "errorCode": 404,
          "details": "User with id=5ac00873-2948-436d-9da0-1fb450fe241b not found"
      }
      """;

  private final UserService userService;

  @Operation(
      summary = "Returns a user",
      operationId = "getUser",
      description = "Returns data of a user from a database",
      responses = {
        @ApiResponse(responseCode = "200", description = "Data of a user"),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(examples = @ExampleObject(USER_NOT_FOUND_ERROR)))
      })
  @GetMapping(value = V1 + USER_ID_PATH)
  public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
    UserDto user = userService.getUser(userId);
    return ResponseEntity.ok(user);
  }
}

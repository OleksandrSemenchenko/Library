package com.nerdysoft.library.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST controller supports end points to manage users.
 *
 * @author Oleksandr Semenchenko
 */
@RestController
@RequiredArgsConstructor
public class UserController {

  private static final String V1 = "/v1";
  private static final String USER_PATH = "/users/{userId}";
  private static final String BOOK_PATH = "/books/{bookId}";

  private static final String USER_NOT_FOUND_ERROR_EXAMPLE =
      """
      {
          "timestamp": "2024-08-13T15:26:57.322006719",
          "errorCode": 404,
          "details": "User with id=5ac00873-2948-436d-9da0-1fb450fe241b not found"
      }
      """;

  private static final String CONFLICT_ERROR_EXAMPLE =
      """
      {
          "timestamp": "2024-08-14T20:52:36.190695546",
          "errorCode": 409,
          "details": "User with id=f0d9bdfc-38e7-4a34-b07f-8216574efbb5 is already related to
      book with id=2decc0bd-9730-4145-b18e-94029dfb961f"
      }
      """;

  private final UserService userService;

  @Operation(
      summary = "Relates a user with a book",
      operationId = "borrowBook",
      description = "Creates in a database relation between a user and a book",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Creates successfully a user book relation"),
        @ApiResponse(
            responseCode = "404",
            description = "A user or a book not found",
            content = @Content(examples = @ExampleObject(USER_NOT_FOUND_ERROR_EXAMPLE))),
        @ApiResponse(
            responseCode = "409",
            description = "Business rules conflicts",
            content = @Content(examples = @ExampleObject(CONFLICT_ERROR_EXAMPLE)))
      })
  @PutMapping(value = V1 + USER_PATH + BOOK_PATH)
  @ResponseStatus(OK)
  public void borrowBook(@PathVariable UUID userId, @PathVariable UUID bookId) {
    userService.borrowBook(userId, bookId);
  }

  @Operation(
      summary = "Returns a user",
      operationId = "getUser",
      description = "Returns data of a user from a database",
      responses = {
        @ApiResponse(responseCode = "200", description = "Data of a user"),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(examples = @ExampleObject(USER_NOT_FOUND_ERROR_EXAMPLE)))
      })
  @GetMapping(value = V1 + USER_PATH, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
    UserDto user = userService.getUserById(userId);
    return ResponseEntity.ok(user);
  }
}

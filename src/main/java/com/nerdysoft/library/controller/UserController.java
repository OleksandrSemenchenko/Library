package com.nerdysoft.library.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.nerdysoft.library.service.UserService;
import com.nerdysoft.library.service.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The REST controller supports end points to manage users.
 *
 * @author Oleksandr Semenchenko
 */
@RestController
@RequiredArgsConstructor
public class UserController {

  private static final String V1 = "/v1";
  private static final String USER_ID_PATH = "/users/{userId}";
  private static final String BOOK_PATH = "/books/{bookId}";
  private static final String USERS_PATH = "/users";

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

  private static final String NOT_VALID_USER_NAME_ERROR_EXAMPLE =
      """
        {
            "timestamp": "2024-08-16T00:12:43.422236202",
            "errorCode": 400,
            "details": {
                "name": "must not be blank"
            }
        }
        """;

  private final UserService userService;

  @Operation(
      summary = "Creates user",
      operationId = "createUser",
      responses = {
        @ApiResponse(responseCode = "201", description = "A user was created successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "A request body contains a not valid username",
            content = @Content(examples = @ExampleObject(NOT_VALID_USER_NAME_ERROR_EXAMPLE)))
      })
  @PostMapping(value = V1 + USERS_PATH, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> createUser(@RequestBody @Validated UserDto userDto) {
    UserDto createdUser = userService.createUser(userDto);
    URI location =
        ServletUriComponentsBuilder.fromCurrentServletMapping()
            .path(V1 + USER_ID_PATH)
            .buildAndExpand(createdUser.getId())
            .toUri();
    return ResponseEntity.created(location).build();
  }

  @Operation(
      summary = "Deletes a user",
      operationId = "deleteUser",
      description = "Deletes a user if their don't have borrowed books",
      responses = {
        @ApiResponse(responseCode = "204", description = "Deletes successfully a user"),
        @ApiResponse(
            responseCode = "404",
            description = "A user not found",
            content = @Content(examples = @ExampleObject(USER_NOT_FOUND_ERROR_EXAMPLE))),
        @ApiResponse(
            responseCode = "409",
            description = "A user has borrowed books",
            content = @Content(examples = @ExampleObject(CONFLICT_ERROR_EXAMPLE)))
      })
  @DeleteMapping(value = V1 + USER_ID_PATH)
  @ResponseStatus(NO_CONTENT)
  public void deleteUser(@PathVariable UUID userId) {
    userService.deleteUser(userId);
  }

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
  @PutMapping(value = V1 + USER_ID_PATH + BOOK_PATH)
  @ResponseStatus(OK)
  public void borrowBookByUser(@PathVariable UUID userId, @PathVariable UUID bookId) {
    userService.borrowBookByUser(userId, bookId);
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
  @GetMapping(value = V1 + USER_ID_PATH, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
    UserDto user = userService.getUserById(userId);
    return ResponseEntity.ok(user);
  }
}

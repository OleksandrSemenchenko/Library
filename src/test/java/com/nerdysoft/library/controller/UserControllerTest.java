package com.nerdysoft.library.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nerdysoft.library.exceptionhandler.exceptions.BookNotFoundException;
import com.nerdysoft.library.exceptionhandler.exceptions.BooksAmountConflictException;
import com.nerdysoft.library.exceptionhandler.exceptions.UserBookRelationConflictException;
import com.nerdysoft.library.exceptionhandler.exceptions.UserNotFoundException;
import com.nerdysoft.library.service.UserService;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

  private static final String V1 = "/v1";
  private static final String USER_PATH = "/users/{userId}";
  private static final String BOOK_PATH = "/books/{bookId}";
  private static final UUID NOT_EXISTING_USER_ID = UUID.randomUUID();
  private static final UUID USER_ID = UUID.randomUUID();
  private static final UUID BOOK_ID = UUID.randomUUID();

  @Autowired private MockMvc mockMvc;

  @MockBean private UserService userService;

  @Test
  void borrowBook_shouldThrowException_whenUserHasMaxBooksQuantity() throws Exception {
    doThrow(UserBookRelationConflictException.class).when(userService).borrowBook(USER_ID, BOOK_ID);

    mockMvc
        .perform(MockMvcRequestBuilders.put(V1 + USER_PATH + BOOK_PATH, USER_ID, BOOK_ID))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.errorCode", Matchers.is(409)))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void borrowBook_shouldThrowException_whenNoAvailableBooks() throws Exception {
    doThrow(BooksAmountConflictException.class).when(userService).borrowBook(USER_ID, BOOK_ID);

    mockMvc
        .perform(MockMvcRequestBuilders.put(V1 + USER_PATH + BOOK_PATH, USER_ID, BOOK_ID))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.errorCode", Matchers.is(409)))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void borrowBook_shouldThrowException_whenNoBookInDb() throws Exception {
    doThrow(BookNotFoundException.class).when(userService).borrowBook(USER_ID, BOOK_ID);

    mockMvc
        .perform(MockMvcRequestBuilders.put(V1 + USER_PATH + BOOK_PATH, USER_ID, BOOK_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.errorCode", Matchers.is(404)))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void borrowBook_shouldThrowException_whenNoUserInDb() throws Exception {
    doThrow(UserNotFoundException.class).when(userService).borrowBook(USER_ID, BOOK_ID);

    mockMvc
        .perform(MockMvcRequestBuilders.put(V1 + USER_PATH + BOOK_PATH, USER_ID, BOOK_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.errorCode", Matchers.is(404)))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void borrowBook_shouldThrowException_whenUserAlreadyHasBook() throws Exception {
    doThrow(UserBookRelationConflictException.class).when(userService).borrowBook(USER_ID, BOOK_ID);

    mockMvc
        .perform(MockMvcRequestBuilders.put(V1 + USER_PATH + BOOK_PATH, USER_ID, BOOK_ID))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.errorCode", Matchers.is(409)))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void getUser_shouldReturnStatus404_whenNoUserInDb() throws Exception {
    when(userService.getUserById(NOT_EXISTING_USER_ID)).thenThrow(UserNotFoundException.class);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(V1 + USER_PATH, NOT_EXISTING_USER_ID)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.errorCode", Matchers.is(404)))
        .andExpect(jsonPath("$.timestamp").exists());
  }
}

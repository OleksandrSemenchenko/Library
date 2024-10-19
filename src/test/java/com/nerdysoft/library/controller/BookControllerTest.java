package com.nerdysoft.library.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdysoft.library.TestDataGenerator;
import com.nerdysoft.library.exceptionhandler.exceptions.BookNotFoundException;
import com.nerdysoft.library.exceptionhandler.exceptions.DeleteBookConflictException;
import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
class BookControllerTest {

  private static final String V1 = "/v1";
  private static final String BOOKS_PATH = "/books";
  private static final String BOOK_ID_PATH = "/books/{bookId}";
  private static final String USER_NAME_PATH = "/users/{userName}";
  private static final UUID BOOK_ID = UUID.randomUUID();
  private static final String USER_NAME = "John Doe";

  @Autowired private MockMvc mockMvc;

  @MockBean private BookService bookService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void updateBook_shouldReturnStatus404_whenNoBookInDb() throws Exception {
    BookDto bookDto = TestDataGenerator.generateBookDto();
    String requestBody = objectMapper.writeValueAsString(bookDto);

    when(bookService.updateBook(any(BookDto.class))).thenThrow(BookNotFoundException.class);

    mockMvc
        .perform(
            put(V1 + BOOK_ID_PATH, bookDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.errorCode", is(404)));
  }

  @Test
  void updateBook_shouldReturnStatus400_whenRequestBodyIsNotValid() throws Exception {
    BookDto bookDto = TestDataGenerator.generateBookDto();
    String notValidAuthorName = "d Flist";
    String notValidTitle = "D";
    bookDto.setAuthor(notValidAuthorName);
    bookDto.setTitle(notValidTitle);
    String requestBody = objectMapper.writeValueAsString(bookDto);

    mockMvc
        .perform(
            put(V1 + BOOK_ID_PATH, bookDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.errorCode", is(400)));
  }

  @Test
  void getBooksBorrowedByUser_shouldReturnStatus404_whenNoBorrowedBooks() throws Exception {
    when(bookService.getBooksBorrowedByUser(USER_NAME)).thenThrow(BookNotFoundException.class);

    mockMvc
        .perform(MockMvcRequestBuilders.get(V1 + USER_NAME_PATH + BOOKS_PATH, USER_NAME))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.errorCode", is(404)));
  }

  @Test
  void deleteBookById_shouldReturnStatus404_whenNoBookInDb() throws Exception {
    doThrow(BookNotFoundException.class).when(bookService).deleteBookById(BOOK_ID);

    mockMvc
        .perform(delete(V1 + BOOK_ID_PATH, BOOK_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.errorCode", is(404)));
  }

  @Test
  void deleteBookById_shouldReturnStatus409_whenBookIsBorrowed() throws Exception {
    doThrow(DeleteBookConflictException.class).when(bookService).deleteBookById(BOOK_ID);

    mockMvc
        .perform(delete(V1 + BOOK_ID_PATH, BOOK_ID))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.details").hasJsonPath())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.errorCode", is(409)));
  }

  @Test
  void addBook_shouldReturnStatus400AndErrorBody_whenBookTitleIsNotValid() throws Exception {
    BookDto bookDto = TestDataGenerator.generateBookDto();
    String notValidBookTitle = "I ";
    bookDto.setTitle(notValidBookTitle);
    String requestBody = objectMapper.writeValueAsString(bookDto);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(V1 + BOOKS_PATH)
                .contentType(APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details").exists())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.errorCode", is(400)));
  }

  @Test
  void addBook_shouldReturnStatus400AndErrorBody_whenAmountIsNotValid() throws Exception {
    BookDto bookDto = TestDataGenerator.generateBookDto();
    int notValidAmount = 0;
    bookDto.setAmount(notValidAmount);
    String requestBody = objectMapper.writeValueAsString(bookDto);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(V1 + BOOKS_PATH)
                .contentType(APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details").exists())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.errorCode", is(400)));
  }

  @Test
  void addBook_shouldReturnStatus400AndErrorBody_whenAuthorNameIsNotValid() throws Exception {
    BookDto bookDto = TestDataGenerator.generateBookDto();
    String notValidName = "Stiven";
    bookDto.setAuthor(notValidName);
    String requestBody = objectMapper.writeValueAsString(bookDto);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(V1 + BOOKS_PATH)
                .contentType(APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details").exists())
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.errorCode", is(400)));
  }
}

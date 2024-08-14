package com.nerdysoft.library.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdysoft.library.TestDataGenerator;
import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
class BookControllerTest {

  private static final String V1 = "/v1";
  private static final String BOOKS_PATH = "/books";

  @Autowired private MockMvc mockMvc;

  @MockBean private BookService bookService;

  @Autowired private ObjectMapper objectMapper;

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

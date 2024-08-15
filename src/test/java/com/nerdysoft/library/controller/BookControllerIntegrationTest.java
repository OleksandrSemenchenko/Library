package com.nerdysoft.library.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdysoft.library.TestDataGenerator;
import com.nerdysoft.library.service.dto.BookDto;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/db/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BookControllerIntegrationTest {

  private static final String V1 = "/v1";
  private static final String BOOKS_PATH = "/books";
  private static final String BOOK_PATH = "/books/{bookId}";
  private static final String USER_NAME_PATH = "/users/{userName}";
  private static final UUID BOOK_ID = UUID.fromString("42d3f123-dd2f-4a10-a182-6506edd9d355");
  private static final String BOOK_ID_BORROWED = "2decc0bd-9730-4145-b18e-94029dfb961f";
  private static final String BOOK_TITLE_BORROWED = "Effective Java";
  private static final String BOOK_AUTHOR_BORROWED = "Bloch Joshua";
  private static final String USER_NAME = "John Doe";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void getBooksBorrowedByUser_shouldReturnStatus200AndBody_whenBooksAreInDb() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(V1 + USER_NAME_PATH + BOOKS_PATH, USER_NAME))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userName", is(USER_NAME)))
        .andExpect(jsonPath("$.books[0].id", is(BOOK_ID_BORROWED)))
        .andExpect(jsonPath("$.books[0].title", is(BOOK_TITLE_BORROWED)))
        .andExpect(jsonPath("$.books[0].author", is(BOOK_AUTHOR_BORROWED)));
  }

  @Test
  void deleteBookById_shouldReturnStatus204_whenBookIsInDb() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.delete(V1 + BOOK_PATH, BOOK_ID))
        .andExpect(status().isNoContent());
  }

  @Test
  void addBook_shouldReturnStatus200AndBody_whenRequestBodyIsValid() throws Exception {
    BookDto bookDto = TestDataGenerator.generateBookDto();
    String requestBody = objectMapper.writeValueAsString(bookDto);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(V1 + BOOKS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(bookDto.getId().toString())))
        .andExpect(jsonPath("$.title", is(bookDto.getTitle())))
        .andExpect(jsonPath("$.author", is(bookDto.getAuthor())))
        .andExpect(jsonPath("$.amount", is(2)));
  }
}

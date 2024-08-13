package com.nerdysoft.library.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdysoft.library.TestDataGenerator;
import com.nerdysoft.library.service.dto.BookDto;
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
@Sql(scripts = "/db/books-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BookControllerIntegrationTest {

  private static final String V1 = "/v1";
  private static final String BOOKS_PATH = "/books";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

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

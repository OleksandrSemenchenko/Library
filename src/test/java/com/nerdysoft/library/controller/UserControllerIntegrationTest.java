package com.nerdysoft.library.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nerdysoft.library.TestDataGenerator;
import com.nerdysoft.library.service.dto.UserDto;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/db/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserControllerIntegrationTest {

  private static final String V1 = "/v1";
  private static final String USER_PATH = "/users/{userId}";
  private static final String BOOK_PATH = "/books/{bookId}";
  private static final UUID USER_ID = UUID.fromString("f0d9bdfc-38e7-4a34-b07f-8216574efbb5");
  private static final UUID BOOK_ID = UUID.fromString("42d3f123-dd2f-4a10-a182-6506edd9d355");

  @Autowired private MockMvc mockMvc;

  @Test
  void borrowBook_shouldReturnStatus200_whenUserBorrowsBook() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.put(V1 + USER_PATH + BOOK_PATH, USER_ID, BOOK_ID))
        .andExpect(status().isOk());
  }

  @Test
  void getUser_shouldReturnStatus200AndBody_whenUserIsInDb() throws Exception {
    UserDto expectedUserDto = TestDataGenerator.generateUserDto();

    mockMvc
        .perform(MockMvcRequestBuilders.get(V1 + USER_PATH, USER_ID).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", Matchers.is(USER_ID.toString())))
        .andExpect(jsonPath("$.name", Matchers.is(expectedUserDto.getName())))
        .andExpect(
            jsonPath(
                "$.membershipDate", Matchers.is(expectedUserDto.getMembershipDate().toString())));
  }
}

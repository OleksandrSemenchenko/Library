package com.nerdysoft.library.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdysoft.library.TestDataGenerator;
import com.nerdysoft.library.repository.UserRepository;
import com.nerdysoft.library.repository.entity.User;
import com.nerdysoft.library.service.dto.UserDto;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
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
  private static final String USER_ID_PATH = "/users/{userId}";
  private static final String USERS_PATH = "/users";
  private static final String BOOK_PATH = "/books/{bookId}";
  private static final UUID USER_ID = UUID.fromString("f0d9bdfc-38e7-4a34-b07f-8216574efbb5");
  private static final UUID BOOK_ID = UUID.fromString("42d3f123-dd2f-4a10-a182-6506edd9d355");
  private static final UUID USER_ID_WITHOUT_BOOKS =
      UUID.fromString("ccc5848f-b32f-44b5-86f1-b51aac112be0");

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private UserRepository userRepository;

  @Test
  @Transactional
  void updateUser_shouldUpdateAndReturnStatus200_whenUserUpdated() throws Exception {
    UserDto userDto = TestDataGenerator.generateUserDto();
    String newName = "Elizabeth Taylor";
    userDto.setName(newName);
    String requestBody = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(put(V1 + USER_ID_PATH, USER_ID).contentType(APPLICATION_JSON).content(requestBody))
        .andExpect(status().isOk());

    User updatedUser = userRepository.findById(USER_ID).get();
    User expectedUser = TestDataGenerator.generateUser();
    expectedUser.setName(newName);

    Assertions.assertEquals(expectedUser, updatedUser);
  }

  @Test
  void createUser_shouldReturnStatus201_whenUserCreated() throws Exception {
    UserDto userDto = TestDataGenerator.generateUserDto();
    String requestBody = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(post(V1 + USERS_PATH).contentType(APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", containsString(V1 + "/users/")));
  }

  @Test
  void deleteUser_shouldReturnStatus204_whenUserIsDeleted() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.delete(V1 + USER_ID_PATH, USER_ID_WITHOUT_BOOKS))
        .andExpect(status().isNoContent());
  }

  @Test
  void borrowBookByUser_shouldReturnStatus200_whenUserBorrowsBook() throws Exception {
    mockMvc
        .perform(put(V1 + USER_ID_PATH + BOOK_PATH, USER_ID, BOOK_ID))
        .andExpect(status().isOk());
  }

  @Test
  void getUser_shouldReturnStatus200AndBody_whenUserIsInDb() throws Exception {
    UserDto expectedUserDto = TestDataGenerator.generateUserDto();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get(V1 + USER_ID_PATH, USER_ID).contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", Matchers.is(USER_ID.toString())))
        .andExpect(jsonPath("$.name", Matchers.is(expectedUserDto.getName())))
        .andExpect(
            jsonPath(
                "$.membershipDate", Matchers.is(expectedUserDto.getMembershipDate().toString())));
  }
}

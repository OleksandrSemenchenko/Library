package com.nerdysoft.library.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "/db/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserRepositoryTest {

  private static final UUID USER_ID = UUID.fromString("f0d9bdfc-38e7-4a34-b07f-8216574efbb5");
  private static final UUID BOOK_ID = UUID.fromString("42d3f123-dd2f-4a10-a182-6506edd9d355");
  private static final UUID BOOK_ID_RELATED_TO_USER =
      UUID.fromString("2decc0bd-9730-4145-b18e-94029dfb961f");

  @Autowired private UserRepository userRepository;

  @Test
  void createUserBookRelation_shouldCreateRelation_whenUserAndBookExist() {
    userRepository.createUserBookRelation(
        USER_ID.toString(), BOOK_ID.toString(), UUID.randomUUID().toString());

    boolean isRelationExists = userRepository.existsByIdAndBooksId(USER_ID, BOOK_ID);
    assertTrue(isRelationExists);
  }

  @Test
  void countBookRelationsById_shouldCountRelations_whenUserBookRelationsExist() {
    int expectedRelationsQuantity = 1;

    int actualRelationQuantity = userRepository.countBookRelationsById(USER_ID.toString());

    assertEquals(expectedRelationsQuantity, actualRelationQuantity);
  }

  @Test
  void existsByIdAndBooksId_shouldReturnFalse_whenUserIsNotRelatedToBook() {
    boolean isRelationExist = userRepository.existsByIdAndBooksId(USER_ID, BOOK_ID);

    assertFalse(isRelationExist);
  }

  @Test
  void existsByIdAndBooksId_shouldReturnTrue_whenUserIsRelatedToBook() {
    boolean isRelationExist = userRepository.existsByIdAndBooksId(USER_ID, BOOK_ID_RELATED_TO_USER);

    assertTrue(isRelationExist);
  }
}

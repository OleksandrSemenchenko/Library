package com.nerdysoft.library.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nerdysoft.library.repository.entity.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "/db/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BookRepositoryTest {

  private static final String BOOK_TITLE = "Clean Code";
  private static final String AUTHOR = "Robert Martin";
  private static final String NOT_EXISTING_BOOK_TITLE = "Code Clean";
  private static final UUID BOOK_ID_OF_USER =
      UUID.fromString("2decc0bd-9730-4145-b18e-94029dfb961f");
  private static final UUID BOOK_ID = UUID.fromString("42d3f123-dd2f-4a10-a182-6506edd9d355");
  private static final String USER_NAME = "John Doe";
  private static final String NOT_EXISTING_USER_NAME = "Denzel Washington";

  @Autowired private BookRepository bookRepository;

  @Test
  void findAllBooksRelatedToUsers_shouldReturnBooksPage_whenUserBookRelationsExist() {
    List<Book> books = bookRepository.findAllBooksRelatedToUsers();

    assertFalse(books.isEmpty());
  }

  @Test
  void findByUsersName_shouldReturnEmptyList_whenNoRelation() {
    List<Book> books = bookRepository.findByUsersName(NOT_EXISTING_USER_NAME);

    assertTrue(books.isEmpty());
  }

  @Test
  void findByUsersName_shouldReturnBooks_whenRelationExist() {
    List<Book> books = bookRepository.findByUsersName(USER_NAME);

    assertFalse(books.isEmpty());
  }

  @Test
  void isBookRelatedToAnyUser_shouldReturnFalse_whenBookHasNoRelation() {
    boolean hasBookRelation = bookRepository.isBookRelatedToAnyUser(BOOK_ID.toString());

    assertFalse(hasBookRelation);
  }

  @Test
  void isBookRelatedToAnyUser_shouldReturnTrue_whenBookHasRelation() {
    boolean hasBookRelation = bookRepository.isBookRelatedToAnyUser(BOOK_ID_OF_USER.toString());

    assertTrue(hasBookRelation);
  }

  @Test
  void findByAuthorAndTitle_shouldReturnEmptyOptional_whenNoBookInDb() {
    Optional<Book> bookOptional =
        bookRepository.findByAuthorAndTitle(AUTHOR, NOT_EXISTING_BOOK_TITLE);

    assertTrue(bookOptional.isEmpty());
  }

  @Test
  void existsByAuthorAndTitle_shouldReturnTrue_whenBookIsInDb() {
    Optional<Book> bookOptional = bookRepository.findByAuthorAndTitle(AUTHOR, BOOK_TITLE);

    assertTrue(bookOptional.isPresent());
  }
}

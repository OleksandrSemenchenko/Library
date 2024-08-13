package com.nerdysoft.library.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "/db/books-data.sql")
class BookRepositoryTest {

  private static final String BOOK_TITLE = "Clean Code";
  private static final String AUTHOR = "Robert Martin";
  private static final String NOT_EXISTING_BOOK_TITLE = "Code Clean";

  @Autowired private BookRepository bookRepository;

  @Test
  void existsByAuthorAndTitle_shouldReturnFalse_whenNoBookInDb() {
    boolean isBookExist = bookRepository.existsByAuthorAndTitle(AUTHOR, NOT_EXISTING_BOOK_TITLE);

    assertFalse(isBookExist);
  }

  @Test
  void existsByAuthorAndTitle_shouldReturnTrue_whenBookIsInDb() {
    boolean isBookExist = bookRepository.existsByAuthorAndTitle(AUTHOR, BOOK_TITLE);

    assertTrue(isBookExist);
  }
}

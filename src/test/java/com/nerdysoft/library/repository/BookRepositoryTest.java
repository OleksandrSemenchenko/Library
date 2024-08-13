package com.nerdysoft.library.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nerdysoft.library.repository.entity.Book;
import java.util.Optional;
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

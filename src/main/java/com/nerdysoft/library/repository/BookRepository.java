package com.nerdysoft.library.repository;

import com.nerdysoft.library.repository.entity.Book;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, UUID> {

  @Query(
      value = "SELECT EXISTS (SELECT ub.* FROM users_books ub WHERE ub.book_id = :bookId)",
      nativeQuery = true)
  boolean isBookRelatedToAnyUser(@Param("bookId") String bookId);

  Optional<Book> findByAuthorAndTitle(String author, String title);
}

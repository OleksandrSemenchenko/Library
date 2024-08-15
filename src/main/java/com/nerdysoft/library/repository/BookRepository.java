package com.nerdysoft.library.repository;

import com.nerdysoft.library.repository.entity.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, UUID> {

  @Query(
      value =
          """
      SELECT b.id, b.title, b.author,
          (SELECT count(ub2.book_id) FROM users_books ub2 WHERE ub2.book_id = b.id) AS amount
        FROM books b JOIN users_books ub ON b.id = ub.book_id
      """,
      nativeQuery = true)
  List<Book> findAllBooksRelatedToUsers();

  List<Book> findByUsersName(String userName);

  @Query(
      value = "SELECT EXISTS (SELECT ub.* FROM users_books ub WHERE ub.book_id = :bookId)",
      nativeQuery = true)
  boolean isBookRelatedToAnyUser(@Param("bookId") String bookId);

  Optional<Book> findByAuthorAndTitle(String author, String title);
}

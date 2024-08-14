package com.nerdysoft.library.repository;

import com.nerdysoft.library.repository.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

  @Modifying
  @Query(
      value =
          """
      INSERT INTO users_books (id, user_id, book_id)
        VALUES(:relationId, :userId, :bookId)
      """,
      nativeQuery = true)
  void createUserBookRelation(
      @Param("userId") String userId,
      @Param("bookId") String bookId,
      @Param("relationId") String relationId);

  @Query(value = "SELECT COUNT(id) FROM users_books ub WHERE user_id = :userId", nativeQuery = true)
  int countBookRelationsByUserId(@Param("userId") String userId);

  boolean existsByIdAndBooksId(UUID userId, UUID bookId);
}

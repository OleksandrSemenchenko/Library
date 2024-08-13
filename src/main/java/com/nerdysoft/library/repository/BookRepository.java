package com.nerdysoft.library.repository;

import com.nerdysoft.library.repository.entity.Book;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, UUID> {

  Optional<Book> findByAuthorAndTitle(String author, String title);
}

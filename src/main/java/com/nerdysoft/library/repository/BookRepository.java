package com.nerdysoft.library.repository;

import com.nerdysoft.library.repository.entity.Book;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, UUID> {

  boolean existsByAuthorAndTitle(String author, String title);
}

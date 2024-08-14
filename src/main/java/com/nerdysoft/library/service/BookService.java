package com.nerdysoft.library.service;

import com.nerdysoft.library.service.dto.BookDto;
import java.util.UUID;

public interface BookService {

  void deleteBookById(UUID bookId);

  BookDto addBook(BookDto book);
}

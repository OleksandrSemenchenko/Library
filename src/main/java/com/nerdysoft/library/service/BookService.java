package com.nerdysoft.library.service;

import com.nerdysoft.library.service.dto.BookDto;
import java.util.UUID;

public interface BookService {

  BookDto decreaseBooksAmountByOne(UUID bookId);

  BookDto getBookById(UUID bookId);

  void deleteBookById(UUID bookId);

  BookDto addBook(BookDto book);
}

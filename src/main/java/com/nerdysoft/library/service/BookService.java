package com.nerdysoft.library.service;

import com.nerdysoft.library.service.dto.BookDto;
import com.nerdysoft.library.service.dto.BookWrapper;
import java.util.UUID;

public interface BookService {

  BookWrapper getBooksBorrowedByUser(String userName);

  BookDto decreaseBookAmountByOne(UUID bookId);

  BookDto getBookById(UUID bookId);

  void deleteBookById(UUID bookId);

  BookDto addBook(BookDto book);
}

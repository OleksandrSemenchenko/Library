package com.nerdysoft.library.service;

import com.nerdysoft.library.service.dto.BookDto;
import com.nerdysoft.library.service.dto.BookWrapper;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

  BookDto updateBook(BookDto bookDto);

  Page<BookDto> getAllBorrowedBooks(Pageable pageable);

  BookWrapper getBooksBorrowedByUser(String userName);

  BookDto decreaseBookAmountByOne(UUID bookId);

  BookDto getBookById(UUID bookId);

  void deleteBookById(UUID bookId);

  BookDto addBook(BookDto book);
}

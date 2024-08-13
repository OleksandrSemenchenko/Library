package com.nerdysoft.library.controller;

import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookController {

  private static final String V1 = "/v1";
  private static final String BOOKS_PATH = "/books";

  private final BookService bookService;

  @PostMapping(value = V1 + BOOKS_PATH)
  public ResponseEntity<BookDto> addBook(@RequestBody @Validated BookDto bookDto) {
    BookDto addedBook = bookService.addBook(bookDto);
    return ResponseEntity.ok(addedBook);
  }
}

package com.nerdysoft.library.service.impl;

import com.nerdysoft.library.repository.BookRepository;
import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  @Override
  public BookDto addBook(BookDto book) {

    return null;
  }
}

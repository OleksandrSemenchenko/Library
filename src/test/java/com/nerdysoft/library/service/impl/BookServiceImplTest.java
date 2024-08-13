package com.nerdysoft.library.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nerdysoft.library.TestDataGenerator;
import com.nerdysoft.library.mapper.BookMapper;
import com.nerdysoft.library.repository.BookRepository;
import com.nerdysoft.library.repository.entity.Book;
import com.nerdysoft.library.service.dto.BookDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

  private static final int EXPECTED_BOOKS_AMOUNT = 2;

  @InjectMocks private BookServiceImpl bookService;

  @Mock private BookRepository bookRepository;

  @BeforeEach
  void setUp() {
    BookMapper bookMapper = Mappers.getMapper(BookMapper.class);
    ReflectionTestUtils.setField(bookService, "bookMapper", bookMapper);
  }

  @Test
  void addBook_shouldIncreaseBooksAmount_whenBookIsInDb() {
    BookDto bookDto = TestDataGenerator.generateBookDto();
    Book book = TestDataGenerator.generateBook();

    when(bookRepository.findByAuthorAndTitle(bookDto.getAuthor(), bookDto.getTitle()))
        .thenReturn(Optional.of(book));
    when(bookRepository.save(any(Book.class))).thenReturn(book);

    BookDto addedBook = bookService.addBook(bookDto);

    bookDto.setAmount(EXPECTED_BOOKS_AMOUNT);
    verifyBook(bookDto, addedBook);
  }

  @Test
  void addBook_shouldCreateBook_whenNoBookInDb() {
    BookDto bookDto = TestDataGenerator.generateBookDto();
    Book book = TestDataGenerator.generateBook();

    when(bookRepository.findByAuthorAndTitle(bookDto.getAuthor(), bookDto.getTitle()))
        .thenReturn(Optional.empty());
    when(bookRepository.save(any(Book.class))).thenReturn(book);

    BookDto addedBook = bookService.addBook(bookDto);

    verifyBook(bookDto, addedBook);
  }

  private void verifyBook(BookDto expectedBook, BookDto actualBook) {
    assertEquals(expectedBook.getId(), actualBook.getId());
    assertEquals(expectedBook.getTitle(), actualBook.getTitle());
    assertEquals(expectedBook.getAuthor(), actualBook.getAuthor());
    assertEquals(expectedBook.getAmount(), actualBook.getAmount());
  }
}

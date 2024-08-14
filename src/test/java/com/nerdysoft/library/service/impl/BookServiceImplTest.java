package com.nerdysoft.library.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nerdysoft.library.TestDataGenerator;
import com.nerdysoft.library.exceptionhandler.exceptions.BookDeletionConflictException;
import com.nerdysoft.library.exceptionhandler.exceptions.BookNotFoundException;
import com.nerdysoft.library.exceptionhandler.exceptions.BooksAmountConflictException;
import com.nerdysoft.library.mapper.BookMapper;
import com.nerdysoft.library.repository.BookRepository;
import com.nerdysoft.library.repository.entity.Book;
import com.nerdysoft.library.service.dto.BookDto;
import java.util.Optional;
import java.util.UUID;
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
  private static final UUID BOOK_ID = UUID.randomUUID();

  @InjectMocks private BookServiceImpl bookService;

  @Mock private BookRepository bookRepository;

  @BeforeEach
  void setUp() {
    BookMapper bookMapper = Mappers.getMapper(BookMapper.class);
    ReflectionTestUtils.setField(bookService, "bookMapper", bookMapper);
  }

  @Test
  void decreaseBooksAmountByOne_shouldDecreaseAmountByOne_whenBooksAmountMoreThanZero() {
    Book book = TestDataGenerator.generateBook();
    BookDto expectedBookDto = TestDataGenerator.generateBookDto();
    expectedBookDto.setAmount(0);

    when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
    when(bookRepository.save(book)).thenReturn(book);

    BookDto updatedBook = bookService.decreaseBooksAmountByOne(BOOK_ID);

    verifyBook(expectedBookDto, updatedBook);
  }

  @Test
  void decreaseBooksAmountByOne_shouldThrowException_whenBooksAmountIsZero() {
    Book book = TestDataGenerator.generateBook();
    book.setAmount(0);

    when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

    assertThrows(
        BooksAmountConflictException.class, () -> bookService.decreaseBooksAmountByOne(BOOK_ID));
  }

  @Test
  void getBookById_shouldReturnBook_whenBookIsInDb() {
    Book book = TestDataGenerator.generateBook();
    BookDto expectedBookDto = TestDataGenerator.generateBookDto();

    when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

    BookDto retrivedBookDto = bookService.getBookById(BOOK_ID);

    verifyBook(expectedBookDto, retrivedBookDto);
  }

  @Test
  void getBookById_shouldThrowException_whenNoBookInDb() {
    when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

    assertThrows(BookNotFoundException.class, () -> bookService.getBookById(BOOK_ID));
  }

  @Test
  void deleteBookById_shouldDeleteBook_whenBookIsInDb() {
    Book book = TestDataGenerator.generateBook();

    when(bookRepository.isBookRelatedToAnyUser(book.getId().toString())).thenReturn(false);
    when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

    bookService.deleteBookById(book.getId());

    verify(bookRepository).delete(book);
  }

  @Test
  void deleteBookById_shouldThrowException_whenNoBookInDb() {
    when(bookRepository.isBookRelatedToAnyUser(BOOK_ID.toString())).thenReturn(false);
    when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

    assertThrows(BookNotFoundException.class, () -> bookService.deleteBookById(BOOK_ID));
  }

  @Test
  void deleteBookById_shouldThrowException_whenBookIsBorrowed() {
    when(bookRepository.isBookRelatedToAnyUser(BOOK_ID.toString())).thenReturn(true);

    assertThrows(BookDeletionConflictException.class, () -> bookService.deleteBookById(BOOK_ID));
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

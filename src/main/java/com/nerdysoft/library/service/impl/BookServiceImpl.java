package com.nerdysoft.library.service.impl;

import static com.nerdysoft.library.exceptionhandler.ExceptionMessages.BOOK_IS_BORROWED;
import static com.nerdysoft.library.exceptionhandler.ExceptionMessages.BOOK_NOT_FOUND;

import com.nerdysoft.library.exceptionhandler.exceptions.BookNotFoundException;
import com.nerdysoft.library.exceptionhandler.exceptions.DeleteForbiddenException;
import com.nerdysoft.library.mapper.BookMapper;
import com.nerdysoft.library.repository.BookRepository;
import com.nerdysoft.library.repository.entity.Book;
import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

  public void deleteBook(UUID bookId) {
    if (bookRepository.isBookRelatedToAnyUser(bookId)) {
      log.debug(BOOK_IS_BORROWED.formatted(bookId));
      throw new DeleteForbiddenException(BOOK_IS_BORROWED.formatted(bookId));
    }
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND.formatted(bookId)));
    bookRepository.delete(book);
  }

  /**
   * Creates a book if it doesn't exist or increases its amount in another case.
   *
   * @param bookDto - a book
   * @return - a created or updated book
   */
  @Override
  @Transactional
  public BookDto addBook(BookDto bookDto) {
    Optional<Book> databaseBookOptional =
        bookRepository.findByAuthorAndTitle(bookDto.getAuthor(), bookDto.getTitle());

    if (databaseBookOptional.isEmpty()) {
      return createBook(bookDto);
    } else {
      return increaseBooksAmount(databaseBookOptional.get(), bookDto.getAmount());
    }
  }

  private BookDto createBook(BookDto bookDto) {
    Book newBook = bookMapper.toEntity(bookDto);
    Book savedBook = bookRepository.save(newBook);
    return bookMapper.toDto(savedBook);
  }

  private BookDto increaseBooksAmount(Book book, int booksAmount) {
    booksAmount += book.getAmount();
    book.setAmount(booksAmount);
    Book updatedBook = bookRepository.save(book);
    return bookMapper.toDto(updatedBook);
  }
}

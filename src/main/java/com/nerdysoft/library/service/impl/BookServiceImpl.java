package com.nerdysoft.library.service.impl;

import static com.nerdysoft.library.exceptionhandler.ExceptionMessages.BOOK_IS_BORROWED;
import static com.nerdysoft.library.exceptionhandler.ExceptionMessages.BOOK_NOT_FOUND;

import com.nerdysoft.library.exceptionhandler.ExceptionMessages;
import com.nerdysoft.library.exceptionhandler.exceptions.BookAmountConflictException;
import com.nerdysoft.library.exceptionhandler.exceptions.BookNotFoundException;
import com.nerdysoft.library.exceptionhandler.exceptions.DeleteBookConflictException;
import com.nerdysoft.library.mapper.BookMapper;
import com.nerdysoft.library.repository.BookRepository;
import com.nerdysoft.library.repository.entity.Book;
import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import com.nerdysoft.library.service.dto.BookWrapper;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

  private static final String GET_BOOK_BY_ID_CACHE = "getBookByIdCache";

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

  @Override
  @Transactional
  @CachePut(value = GET_BOOK_BY_ID_CACHE, key = "#bookDto.id")
  public BookDto updateBook(BookDto bookDto) {
    Book book = findBookById(bookDto.getId());
    Book updatedBook = bookMapper.mergeWithDto(bookDto, book);
    Book savedBook = bookRepository.save(updatedBook);
    return bookMapper.toDto(savedBook);
  }

  @Override
  public Page<BookDto> getAllBorrowedBooks(Pageable pageable) {
    List<Book> books = bookRepository.findAllBooksRelatedToUsers();
    List<BookDto> bookDtos = bookMapper.toDtoList(books);
    return new PageImpl<>(bookDtos, pageable, bookDtos.size());
  }

  @Override
  public BookWrapper getBooksBorrowedByUser(String userName) {
    List<Book> borrowedBooks = bookRepository.findByUsersName(userName);

    if (borrowedBooks.isEmpty()) {
      log.debug(ExceptionMessages.BOOK_NOT_FOUND_BY_USER_NAME.formatted(userName));
      throw new BookNotFoundException(
          ExceptionMessages.BOOK_NOT_FOUND_BY_USER_NAME.formatted(userName));
    }
    List<BookDto> borrowedBookDtos =
        borrowedBooks.stream()
            .map(
                book -> {
                  book.setAmount(null);
                  return book;
                })
            .map(bookMapper::toDto)
            .toList();

    return BookWrapper.builder().userName(userName).books(borrowedBookDtos).build();
  }

  @Override
  @Transactional
  @CacheEvict(value = GET_BOOK_BY_ID_CACHE, key = "#bookId")
  public BookDto decreaseBookAmountByOne(UUID bookId) {
    Book book = findBookById(bookId);
    int booksAmount = book.getAmount();

    if (booksAmount == 0) {
      log.debug(ExceptionMessages.ZERO_BOOKS_AMOUNT);
      throw new BookAmountConflictException(ExceptionMessages.ZERO_BOOKS_AMOUNT);
    }
    booksAmount--;
    book.setAmount(booksAmount);
    Book savedBook = bookRepository.save(book);
    return bookMapper.toDto(savedBook);
  }

  @Override
  @Cacheable(value = GET_BOOK_BY_ID_CACHE, key = "#bookId")
  public BookDto getBookById(UUID bookId) {
    Book book = findBookById(bookId);
    return bookMapper.toDto(book);
  }

  private Book findBookById(UUID bookId) {
    return bookRepository
        .findById(bookId)
        .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND.formatted(bookId)));
  }

  @Override
  @Transactional
  @CacheEvict(value = GET_BOOK_BY_ID_CACHE, key = "#bookId")
  public void deleteBookById(UUID bookId) {
    if (bookRepository.isBookRelatedToAnyUser(bookId.toString())) {
      log.debug(BOOK_IS_BORROWED.formatted(bookId));
      throw new DeleteBookConflictException(BOOK_IS_BORROWED.formatted(bookId));
    }
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND.formatted(bookId)));
    bookRepository.delete(book);
  }

  /**
   * If a book is added with the same title and author that already exists in the database, the
   * amount of the existing book is increased by an amount in a request body. A new book is created
   * if the database has no book with such a title and an author.
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
      return increaseBookAmount(databaseBookOptional.get(), bookDto.getAmount());
    }
  }

  private BookDto createBook(BookDto bookDto) {
    Book newBook = bookMapper.toEntity(bookDto);
    Book savedBook = bookRepository.save(newBook);
    return bookMapper.toDto(savedBook);
  }

  private BookDto increaseBookAmount(Book book, int booksAmount) {
    booksAmount += book.getAmount();
    book.setAmount(booksAmount);
    Book updatedBook = bookRepository.save(book);
    return bookMapper.toDto(updatedBook);
  }
}

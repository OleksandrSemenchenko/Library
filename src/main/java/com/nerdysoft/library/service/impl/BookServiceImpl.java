package com.nerdysoft.library.service.impl;

import com.nerdysoft.library.mapper.BookMapper;
import com.nerdysoft.library.repository.BookRepository;
import com.nerdysoft.library.repository.entity.Book;
import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

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

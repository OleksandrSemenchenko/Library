package com.nerdysoft.library.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import com.nerdysoft.library.service.dto.BookWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST controller supports end points to manage books.
 *
 * @author Oleksandr Semenchenko
 */
@RestController
@RequiredArgsConstructor
public class BookController {

  private static final String V1 = "/v1";
  private static final String BOOKS_PATH = "/books";
  private static final String BOOKS_BORROWED_PATH = "/books/borrowed";
  private static final String BOOK_PATH = "/books/{bookId}";
  private static final String USER_NAME_PATH = "/users/{userName}";

  private static final String BAD_REQUEST_ERROR_EXAMPLE =
      """
      {
          "timestamp": "2024-08-14T09:37:10.805728923",
          "errorCode": 400,
          "details": {
              "author": "The author name should contain two capital words with name and surname and space between them"
          }
      }
      """;
  private static final String FORBIDDEN_ERROR_EXAMPLE =
      """
      {
          "timestamp": "2024-08-14T13:43:42.852665792",
          "errorCode": 403,
          "details": "The book with id=2decc0bd-9730-4145-b18e-94029dfb961f cannot be deleted because it is borrowed"
      }
      """;
  private static final String BOOK_NOT_FOUND_ERROR_EXAMPLE =
      """
      {
          "timestamp": "2024-08-14T14:42:59.607396668",
          "errorCode": 404,
          "details": "Book with id=a30963ae-8a32-4c26-a83b-0eb8ff2c8a1b nod found"
      }
      """;
  private static final String BOOKS_BORROWED_NOT_FOUND =
      """
      {
          "timestamp": "2024-08-15T09:46:55.607145702",
          "errorCode": 404,
          "details": "Books borrowed by user with name 'John Smith' not found"
      }
      """;

  private final BookService bookService;

  @GetMapping(value = V1 + BOOKS_BORROWED_PATH, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<BookDto>> getAllBorrowedBooks(Pageable pageable) {
    Page<BookDto> borrowedBooks = bookService.getAllBorrowedBooks(pageable);
    return ResponseEntity.ok(borrowedBooks);
  }

  @Operation(
      summary = "Return books",
      operationId = "getBooksBorrowedByUser",
      description = "Returns books borrowed by user found by their name",
      responses = {
        @ApiResponse(responseCode = "200", description = "The list of books"),
        @ApiResponse(responseCode = "404", description = "Books not found borrowed by a user")
      })
  @GetMapping(value = V1 + USER_NAME_PATH + BOOKS_PATH, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<BookWrapper> getBooksBorrowedByUser(@PathVariable String userName) {
    BookWrapper userBooks = bookService.getBooksBorrowedByUser(userName);
    return ResponseEntity.ok(userBooks);
  }

  /**
   * Deletes a book if it is not related to any user.
   *
   * @param bookId - book ID
   */
  @Operation(
      summary = "Deletes a book",
      operationId = "deleteBookById",
      description = "Deletes a book from a database if it does not have relations to any user",
      responses = {
        @ApiResponse(responseCode = "204", description = "The book was successful deleted"),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden to delete a book",
            content = @Content(examples = @ExampleObject(FORBIDDEN_ERROR_EXAMPLE))),
        @ApiResponse(
            responseCode = "404",
            description = "The book not found",
            content = @Content(examples = @ExampleObject(BOOK_NOT_FOUND_ERROR_EXAMPLE)))
      })
  @DeleteMapping(value = V1 + BOOK_PATH)
  @ResponseStatus(NO_CONTENT)
  public void deleteBookById(@PathVariable UUID bookId) {
    bookService.deleteBookById(bookId);
  }

  @Operation(
      summary = "Add book to a database",
      operationId = "addBook",
      description = "Creates or updates book in a database",
      responses = {
        @ApiResponse(responseCode = "200", description = "Book data"),
        @ApiResponse(
            responseCode = "400",
            description = "Not valid data in a request body",
            content = @Content(examples = @ExampleObject(BAD_REQUEST_ERROR_EXAMPLE)))
      })
  @PostMapping(
      value = V1 + BOOKS_PATH,
      consumes = APPLICATION_JSON_VALUE,
      produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<BookDto> addBook(@RequestBody @Validated BookDto bookDto) {
    BookDto addedBook = bookService.addBook(bookDto);
    return ResponseEntity.ok(addedBook);
  }
}

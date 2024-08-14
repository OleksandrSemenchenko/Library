package com.nerdysoft.library.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  private static final String BOOK_PATH = "/books/{bookId}";

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
  private static final String NOT_FOUND_ERROR_EXAMPLE =
      """
      {
          "timestamp": "2024-08-14T14:42:59.607396668",
          "errorCode": 404,
          "details": "Book with id=a30963ae-8a32-4c26-a83b-0eb8ff2c8a1b nod found"
      }
      """;

  private final BookService bookService;

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
            content = @Content(examples = @ExampleObject(NOT_FOUND_ERROR_EXAMPLE)))
      })
  @DeleteMapping(value = V1 + BOOK_PATH)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteBookById(@PathVariable UUID bookId) {
    bookService.deleteBookById(bookId);
  }

  /**
   * If a book is added with the same title and author that already exists in the database, the
   * amount of the existing book is increased by an amount in a request body. A new book is created
   * if the database has no book with such a title and an author.
   *
   * @param bookDto - a book
   * @return ResponseEntity<BookDto> - created or updated book data
   */
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

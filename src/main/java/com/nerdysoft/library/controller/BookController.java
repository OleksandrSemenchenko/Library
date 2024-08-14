package com.nerdysoft.library.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.nerdysoft.library.service.BookService;
import com.nerdysoft.library.service.dto.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  private static final String ADD_BOOK_BAD_REQUEST_ERROR_EXAMPLE =
      """
      {
          "timestamp": "2024-08-14T09:37:10.805728923",
          "errorCode": 400,
          "details": {
              "author": "The author name should contain two capital words with name and surname and space between them"
          }
      }
      """;

  private final BookService bookService;

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
            content = @Content(examples = @ExampleObject(ADD_BOOK_BAD_REQUEST_ERROR_EXAMPLE)))
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

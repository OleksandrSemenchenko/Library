package com.nerdysoft.library.exceptionhandler;

/**
 * Constants to retain exception message templates.
 *
 * @author Oleksandr Semenchenko
 */
public class ExceptionMessages {

  public static final String USER_NOT_FOUND_BY_ID = "User with id=%s not found";
  public static final String BOOK_IS_BORROWED =
      "The book with id=%s cannot be deleted because it is borrowed";
  public static final String BOOK_NOT_FOUND = "Book with id=%s nod found";
  public static final String BOOK_NOT_FOUND_BY_USER_NAME =
      "Books borrowed by user with name '%s' not found";
  public static final String USER_BOOK_RELATION_ALREADY_EXISTS =
      "User with id=%s is already related to book with id=%s";
  public static final String NO_BOOKS = "No available books";
  public static final String MAX_USER_BOOKS_QUANTITY = "A user can't borrow books more than %s";
  public static final String ZERO_BOOKS_AMOUNT = "The books amount cannot be less than zero";
  public static final String USER_BORROWED_BOOKS =
      "The user with id=%s has %s borrowed books and cannot be deleted";

  private ExceptionMessages() {}
}

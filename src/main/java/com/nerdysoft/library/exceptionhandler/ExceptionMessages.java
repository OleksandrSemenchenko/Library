package com.nerdysoft.library.exceptionhandler;

/**
 * Constants to retain exception message templates.
 *
 * @author Oleksandr Semenchenko
 */
public class ExceptionMessages {

  public static final String USER_NOT_FOUND = "User with id=%s not found";
  public static final String BOOK_IS_BORROWED =
      "The book with id=%s cannot be deleted because it is borrowed";
  public static final String BOOK_NOT_FOUND = "Book with id=%s nod found";

  private ExceptionMessages() {}
}

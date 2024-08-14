package com.nerdysoft.library.exceptionhandler.exceptions;

public class BookDeletionConflictException extends ConflictException {

  public BookDeletionConflictException(String message) {
    super(message);
  }
}

package com.nerdysoft.library.exceptionhandler.exceptions;

public class DeleteBookConflictException extends ConflictException {

  public DeleteBookConflictException(String message) {
    super(message);
  }
}

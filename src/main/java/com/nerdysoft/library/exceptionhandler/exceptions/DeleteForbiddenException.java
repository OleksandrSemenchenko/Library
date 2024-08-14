package com.nerdysoft.library.exceptionhandler.exceptions;

public class DeleteForbiddenException extends ActionForbiddenException {

  public DeleteForbiddenException(String message) {
    super(message);
  }
}
